package org.toylang.compiler;

import org.toylang.antlr.Errors;
import org.toylang.antlr.ToyParser;
import org.toylang.antlr.ToyTree;
import org.toylang.antlr.ast.*;
import org.toylang.core.TLFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

public class Compiler {

    public static final String BIN = "target/classes/";

    private File file;
    private String name;
    private ToyTree tree;

    private static ArrayList<String> IMPORTS = new ArrayList<>();

    private ArrayList<ToyTree> trees = new ArrayList<>();
    private ArrayList<String> classPath;

    public Compiler(String file, String name, ToyTree tree) {
        this.file = new File(file);
        if(tree.getPackage() != null) {
            this.name = tree.getPackage().toString() + "." + name;
        } else {
            this.name = name;
        }
        this.tree = tree;
        IMPORTS.add(this.name);
        trees.add(tree);
    }
    public HashMap<String, byte[]> compile() throws IOException {
        return compile(true);
    }
    public HashMap<String, byte[]> compile(boolean save) throws IOException {
        HashMap<String, byte[]> classDefinitions = new HashMap<>();

        for (QualifiedName qualifiedName : tree.getImports()) {
            if(!IMPORTS.contains(qualifiedName.toString())) {
                String file = findFile(qualifiedName);

                if(file != null) {
                    try {
                        ToyParser parser = new ToyParser(file);
                        ToyTree tree = parser.parse();
                        trees.add(tree);
                        IMPORTS.add(tree.getPackage().add(tree.getName()).toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Class clazz = Class.forName(qualifiedName.toString());
                        IMPORTS.add(qualifiedName.toString());
                        buildSymbolMap(clazz);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if(Errors.getErrorCount() > 0) {
            Errors.printErrors();
            System.err.println("Compilation failed with "+Errors.getErrorCount() + " errors.");
        } else {
            // no errors... generate code
            HashMap<String, ClassMaker> classes = new HashMap<>();
            for (ToyTree toyTree : trees) {
                for (ClassDef classDef : toyTree.getClasses()) {
                    classDef.setPackage(toyTree.getPackage());
                    String name = toyTree.getPackage().add(classDef.getName()).toString();
                    ClassMaker cm = new ClassMaker(classDef, toyTree.getImports());
                    if(!name.equals(toyTree.getFullName().toString())) {
                        Fun clinit = new Fun(new QualifiedName("<clinit>"), new Block(), null, null, null);
                        cm.addStaticMethods(clinit);
                        cm.make();
                    }
                    classes.put(name, cm);
                }
                QualifiedName name = toyTree.getFullName();
                ClassMaker cm;
                // find the correct class to put that statics in
                if(classes.containsKey(name.toString())) {
                    cm = classes.get(name.toString());
                } else {
                    cm = new ClassMaker(toyTree.getPackage(), toyTree.getName().toString(), toyTree.getImports());
                    classes.put(name.toString(), cm);
                }
                Block b = new Block();
                Fun clinit = new Fun(new QualifiedName("<clinit>"), b, null, null, null);
                for (Statement statement : toyTree.getStatements()) {
                    if(statement instanceof VarDecl) {
                        cm.addStaticFields((VarDecl) statement);
                    }
                    if(!(statement instanceof Fun)) {
                        b.append(statement);
                    } else {
                        cm.addStaticMethods((Fun)statement);
                    }
                }
                cm.addStaticMethods(clinit); // clinit MUST be the last to be added
                cm.make();
            }
            if(Errors.getErrorCount() > 0) {
                Errors.printErrors();
            } else {
                for (String s : classes.keySet()) {
                    String file = BIN + "/" + s.replace(".", "/") + ".class";
                    byte[] data = classes.get(s).getBytes();
                    classDefinitions.put(s, data);
                    if(save) {
                        File f = new File(file).getParentFile();
                        f.mkdirs();
                        FileOutputStream fos = new FileOutputStream(file);
                        fos.write(data);
                        fos.close();
                    }
                }
            }
        }
        return classDefinitions;
    }
    private final String findFile(QualifiedName name) {
        QualifiedName pack = tree.getPackage();
        File basePath = file.getParentFile();
        if(pack != null) {
            for (int i = 0; i < pack.getNames().length; i++) {
                basePath = basePath.getParentFile();
            }
        }
        String str = basePath.getPath() + "/" +(name.toString().replace('.', '/')) + ".tl";
        if(!new File(str).exists())
            return null;
        return str;
    }
    public static void buildSymbolMap(Class clazz) {
        boolean isToyLang = clazz.getDeclaredAnnotation(TLFile.class) != null;
        for (Method method : clazz.getMethods()) {
            SymbolMap.FUN_MAP.put(clazz.getPackage().getName() + "." + method.getName(), new Fun(isToyLang ? new QualifiedName(method.getName()) : null,null,null,null));
        }
        for (Constructor constructor : clazz.getConstructors()) {
            // todo;
            if(Modifier.isProtected(constructor.getModifiers()) || Modifier.isPublic(constructor.getModifiers())) {
                //SymbolMap.CLASS_MAP.put(clazz.getName(), new ClassDef(null,null,null,null,null));
                break;
            }
        }
        // todo;
        for (Field field : clazz.getFields()) {
            if(Modifier.isStatic(field.getModifiers())) {
                //SymbolMap.VARIABLE_MAP.put(clazz.getPackage().getName() + "." + field.getName(), new VarDecl(null,null,null));
            }
        }
    }
}