package org.toylang.compiler;

import org.toylang.antlr.ToyParser;
import org.toylang.antlr.ToyTree;
import org.toylang.antlr.ast.*;
import org.toylang.core.TLFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Compiler {

    public static final String BIN = "target/classes/";

    private File file;
    private String name;
    private ToyTree tree;

    private static HashMap<String, byte[]> classMap = new HashMap<>();
    private static ArrayList<String> IMPORTS = new ArrayList<>();

    private ArrayList<ToyTree> trees = new ArrayList<>();
    private ArrayList<String> classPath;

    public Compiler(String file, String name, ToyTree tree) {
        this.file = new File(file);
        if (tree.getPackage() != null) {
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

        for (QualifiedName qualifiedName : tree.getImports()) {
            if (!IMPORTS.contains(qualifiedName.toString())) {
                String file = findFile(qualifiedName);

                if (file != null) {
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
                        Errors.put("Cannot resolve import: " + qualifiedName);
                    }
                }
            }
        }

        if (Errors.getErrorCount() == 0) {
            // no errors... generate code
            HashMap<String, ClassMaker> classes = new HashMap<>();
            for (ToyTree toyTree : trees) {
                if (classMap.containsKey(toyTree.getPackage().add(toyTree.getName()).toString()))
                    continue;
                for (ClassDef classDef : toyTree.getClasses()) {
                    classDef.setPackage(toyTree.getPackage());
                    classDef.setSourceTree(toyTree);
                    String name = toyTree.getPackage().add(classDef.getName()).toString();

                    List<QualifiedName> lst = toyTree.getImports();

                    for (ClassDef cd : toyTree.getClasses()) {
                        if (cd != classDef) {
                            lst.add(toyTree.getPackage().add(classDef.getName()));
                        }
                    }
                    ClassMaker cm = new ClassMaker(classDef, lst);
                    toyTree.addImport(toyTree.getPackage().add(classDef.getName()));
                    toyTree.addImport(toyTree.getPackage().add(toyTree.getName()));
                    if (!name.equals(toyTree.getFullName().toString())) {
                        Fun clinit = new Fun(new QualifiedName("<clinit>"), new Block(), null, null);
                        cm.addStaticMethods(clinit);
                        cm.make();
                    }
                    classes.put(name, cm);
                }
                QualifiedName name = toyTree.getFullName();
                ClassMaker cm;
                // find the correct class to put that statics in
                if (classes.containsKey(name.toString())) {
                    cm = classes.get(name.toString());
                } else {
                    cm = new ClassMaker(toyTree.getPackage(), toyTree.getName().toString(), toyTree.getImports());
                    classes.put(name.toString(), cm);
                }
                Block b = new Block();
                Fun clinit = new Fun(new QualifiedName("<clinit>"), b, null, null, null);
                for (Statement statement : toyTree.getStatements()) {
                    if (statement instanceof VarDecl) {
                        cm.addStaticFields((VarDecl) statement);
                    }
                    if (!(statement instanceof Fun)) {
                        b.append(statement);
                    } else {
                        cm.addStaticMethods((Fun) statement);
                    }
                }
                cm.addStaticMethods(clinit); // clinit MUST be the last to be added
                cm.make();
            }
            if (Errors.getErrorCount() > 0) {
                classMap.clear();
            } else {
                for (String s : classes.keySet()) {
                    String file = BIN + "/" + s.replace(".", "/") + ".class";
                    byte[] data = classes.get(s).getBytes();
                    classMap.put(s, data);
                    if (save) {
                        File f = new File(file).getParentFile();
                        f.mkdirs();
                        FileOutputStream fos = new FileOutputStream(file);
                        fos.write(data);
                        fos.close();
                    }
                }
            }
        }
        return classMap;
    }

    private String findFile(QualifiedName name) {
        QualifiedName pack = tree.getPackage();
        File basePath = file.getParentFile();
        if (pack != null) {
            for (int i = 0; i < pack.getNames().length; i++) {
                basePath = basePath.getParentFile();
            }
        }
        String str = basePath.getPath() + "/" + (name.toString().replace('.', '/')) + ".tl";
        if (!new File(str).exists())
            return null;
        return str;
    }

    private static void buildSymbolMap(Class clazz) {
        boolean isJava = clazz.getDeclaredAnnotation(TLFile.class) == null;
        if (isJava)
            return;
        for (Method method : clazz.getMethods()) {
            if (Modifier.isPublic(method.getModifiers())) {
                SymbolMap.FUN_MAP.put(clazz.getPackage().getName() + "." + method.getName(), new Fun(new QualifiedName(method.getName()), null, null, null));
            }
        }
    }
}