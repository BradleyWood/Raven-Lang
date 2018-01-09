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
import java.util.*;

public class Compiler {

    public static final String BIN = "target/classes/";

    private File file;
    private String name;
    private ToyTree tree;

    private static LinkedList<AnnotationProcessor> processors = new LinkedList<>();
    private static HashMap<String, byte[]> classMap = new HashMap<>();
    private static ArrayList<String> IMPORTS = new ArrayList<>();

    private ArrayList<ToyTree> trees = new ArrayList<>();
    private ArrayList<String> classPath;

    public Compiler(String file, String name, ToyTree tree, AnnotationProcessor... annotationProcessors) {
        this.file = new File(file);
        if (tree.getPackage() != null) {
            this.name = tree.getPackage().toString() + "." + name;
        } else {
            this.name = name;
        }
        this.tree = tree;
        IMPORTS.add(this.name);
        trees.add(tree);
        Arrays.stream(annotationProcessors).forEach(this::addAnnotationProcessor);
    }

    public void addAnnotationProcessor(AnnotationProcessor annotationProcessor) {
        processors.add(annotationProcessor);
    }

    public List<AnnotationProcessor> getAnnotationProcessors() {
        return processors;
    }

    public HashMap<String, byte[]> compile() throws IOException {
        return compile(true);
    }

    private void parse() {
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
    }

    private ClassMaker setupClassMaker(ToyTree tree, ClassDef classDef) {
        ClassMaker cm;

        if (classDef == null) {
            cm = new ClassMaker(tree.getPackage(), tree.getName().toString(), tree.getImports());
            Block b = new Block();
            Fun clinit = new Fun(new QualifiedName("<clinit>"), b, null, null);
            for (Statement statement : tree.getStatements()) {
                if (statement instanceof VarDecl) {
                    cm.addStaticFields((VarDecl) statement);
                }
                if (!(statement instanceof Fun)) {
                    b.append(statement);
                } else {
                    cm.addStaticMethods((Fun) statement);
                }
            }
            cm.addStaticMethods(clinit);
            return cm;
        }

        classDef.setPackage(tree.getPackage());
        classDef.setSourceTree(tree);
        String name = tree.getPackage().add(classDef.getName()).toString();

        List<QualifiedName> lst = tree.getImports();

        for (ClassDef cd : tree.getClasses()) {
            if (cd != classDef) {
                lst.add(tree.getPackage().add(classDef.getName()));
            }
        }
        cm = new ClassMaker(classDef, lst);
        tree.addImport(tree.getPackage().add(classDef.getName()));
        tree.addImport(tree.getPackage().add(tree.getName()));
        if (!name.equals(tree.getFullName().toString())) {
            Fun clinit = new Fun(new QualifiedName("<clinit>"), new Block(), null, null);
            cm.addStaticMethods(clinit);
            cm.make();
        }
        return cm;
    }

    public HashMap<String, byte[]> compile(boolean save) throws IOException {
        int max = tree.getStatements().size();
        for (int i = 0; i < max; i++) {
            Statement statement = tree.getStatements().get(i);
            getAnnotationProcessors().forEach(processors -> processors.process(tree, statement));
        }

        parse();

        if (Errors.getErrorCount() > 0) {
            // syntax errors
            return classMap;
        }

        // no errors... generate code
        HashMap<String, ClassMaker> classBuilders = new HashMap<>();
        ClassMaker cm;
        for (ToyTree tree : trees) {
            if (classMap.containsKey(tree.getPackage().add(tree.getName()).toString()))
                continue;
            for (ClassDef classDef : tree.getClasses()) {
                cm = setupClassMaker(tree, classDef);
                classBuilders.put(tree.getPackage().add(classDef.getName()).toString(), cm);
            }
            if (classBuilders.containsKey(name)) {
                cm = classBuilders.get(name);
            } else {
                cm = setupClassMaker(tree, null);
                classBuilders.put(name, cm);
            }
            cm.make();
        }
        if (Errors.getErrorCount() > 0) {
            classMap.clear();
            return classMap;
        }

        for (String s : classBuilders.keySet()) {
            String file = BIN + "/" + s.replace(".", "/") + ".class";
            byte[] data = classBuilders.get(s).getBytes();
            classMap.put(s, data);
            if (save) {
                File f = new File(file).getParentFile();
                f.mkdirs();
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(data);
                fos.close();
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