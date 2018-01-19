package org.toylang.compiler;

import org.toylang.antlr.Modifier;
import org.toylang.antlr.ToyParser;
import org.toylang.antlr.ToyTree;
import org.toylang.antlr.ast.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Compiler {

    public static final String BIN = "target/classes/";

    private File file;
    private String name;
    private ToyTree tree;

    private static HashMap<String, byte[]> classMap = new HashMap<>();
    private static ArrayList<String> IMPORTS = new ArrayList<>();

    private LinkedList<AnnotationProcessor> processors = new LinkedList<>();
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

    public void removeAnnotationProcessor(AnnotationProcessor annotationProcessor) {
        processors.remove(annotationProcessor);
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
                        SymbolMap.map(clazz);
                    } catch (ClassNotFoundException e) {
                        Errors.put("Cannot resolve import: " + qualifiedName);
                    }
                }
            }
        }
    }

    public HashMap<String, byte[]> compile(boolean save) throws IOException {
        int max = tree.getStatements().size();
        for (int i = 0; i < max; i++) {
            Statement statement = tree.getStatements().get(i);
            processors.forEach(processors -> processors.process(tree, statement));
        }

        parse();

        if (Errors.getErrorCount() > 0) {
            // syntax errors
            return classMap;
        }

        // no errors... generate code
        HashMap<String, ClassMaker> classBuilders = new HashMap<>();

        modifyTree(tree);

        for (ClassDef classDef : tree.getClasses()) {
            SymbolMap.map(classDef);
            classDef.setPackage(tree.getPackage());
            tree.addImport(tree.getPackage().add(classDef.getName()));
            classDef.setSourceTree(tree);
        }

        for (ClassDef classDef : tree.getClasses()) {
            ClassMaker cm = new ClassMaker(classDef, tree.getImports());
            cm.make();
            classBuilders.put(tree.getPackage().add(classDef.getName()).toString(), cm);
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

    private void modifyTree(ToyTree tree) {
        String name = tree.getPackage().add(tree.getName().toString()).toString().replace(".", "/");
        boolean exists = tree.getClasses().stream().filter(cd -> cd.getFullName().equals(name)).count() == 1;
        ClassDef def;
        if (exists) {
            def = tree.getClasses().stream().filter(cd -> cd.getFullName().equals(name)).collect(Collectors.toList()).get(0);
        } else {
            def = new ClassDef(new Modifier[]{Modifier.PUBLIC}, tree.getPackage(), tree.getName().toString(),
                    new QualifiedName("java", "lang", "Object"), new QualifiedName[0], new ArrayList<>());
        }
        Block b = new Block();
        Fun clinit = new Fun(new QualifiedName("<clinit>"), b, new Modifier[]{Modifier.STATIC}, null);
        for (Statement statement : tree.getStatements()) {
            if (statement instanceof VarDecl) {
                VarDecl decl = (VarDecl) statement;
                decl.addModifier(Modifier.STATIC);
                def.getStatements().add(statement);
            }
            if (!(statement instanceof Fun)) {
                b.append(statement);
            } else {
                Fun fun = (Fun) statement;
                fun.addModifier(Modifier.STATIC);
                def.getStatements().add(statement);
            }
        }
        def.getStatements().add(clinit);
        tree.getStatements().add(def);

        for (ClassDef classDef : tree.getClasses()) {
            if (!classDef.equals(def)) {
                Fun clinit_ = new Fun(new QualifiedName("<clinit>"), new Block(), new Modifier[]{Modifier.STATIC}, null);
                classDef.getStatements().add(clinit_);
            }
        }
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

}
