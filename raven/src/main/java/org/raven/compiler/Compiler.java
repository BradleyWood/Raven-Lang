package org.raven.compiler;

import org.raven.antlr.Modifier;
import org.raven.antlr.Operator;
import org.raven.antlr.RParser;
import org.raven.antlr.RavenTree;
import org.raven.antlr.ast.*;
import org.raven.error.Errors;
import org.raven.util.Settings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class Compiler {

    private static final String DEFUALT_OUTPUT = "target/classes/";

    private File file;
    private String name;
    private RavenTree tree;

    private static HashMap<String, byte[]> classMap = new HashMap<>();
    private static ArrayList<String> IMPORTS = new ArrayList<>();

    private LinkedList<AnnotationProcessor> processors = new LinkedList<>();
    private ArrayList<RavenTree> trees = new ArrayList<>();
    private ArrayList<String> classPath;

    public Compiler(final String file, final String name, final RavenTree tree, final AnnotationProcessor... annotationProcessors) {
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

    public void addAnnotationProcessor(final AnnotationProcessor annotationProcessor) {
        processors.add(annotationProcessor);
    }

    public void removeAnnotationProcessor(final AnnotationProcessor annotationProcessor) {
        processors.remove(annotationProcessor);
    }

    public HashMap<String, byte[]> compile() throws IOException {
        return compile(true);
    }

    private void parseImports() {
        for (QualifiedName qualifiedName : tree.getImports()) {
            if (!IMPORTS.contains(qualifiedName.toString())) {
                String file = findFile(qualifiedName);

                if (file != null) {
                    try {
                        RParser parser = new RParser(file);
                        RavenTree tree = parser.parse();

                        if (tree == null)
                            continue;

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

    public HashMap<String, byte[]> compile(final boolean save) throws IOException {
        parseImports();

        if (Errors.getErrorCount() > 0) {
            // syntax errors
            return classMap;
        }

        // no errors... generate code
        HashMap<String, ClassMaker> classBuilders = new HashMap<>();

        modifyTree(tree);

        List<ClassDef> gga = tree.getClasses();

        for (ClassDef classDef : tree.getClasses()) {
            if (!classDef.isPrivate()) {
                classDef.setPublic();
            }
            SymbolMap.map(classDef);
            tree.addImport(tree.getPackage().add(classDef.getName()));
            classDef.setSourceTree(tree);

            LinkedList<Statement> statements = new LinkedList<>(classDef.getStatements());
            statements.forEach(stmt -> stmt.getAnnotations().forEach(a -> processors.forEach(p -> p.process(a))));
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
            byte[] data = classBuilders.get(s).getBytes();
            classMap.put(s, data);
            if (save) {
                String file = Settings.getOrDefault("OUT", DEFUALT_OUTPUT) + "/" +
                        s.replace(".", "/") + ".class";
                File f = new File(file).getParentFile();
                f.mkdirs();
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(data);
                fos.close();
            }
        }

        return classMap;
    }

    /**
     * Places any non-static fields and functions into a synthetic class with
     * the same name as the containing file. Adds class initializer to any
     * class that does not have one.
     *
     * @param tree The ast
     */
    private void modifyTree(final RavenTree tree) {
        String className = tree.getPackage().add(tree.getName().toString()).toString().replace(".", "/");

        ClassDef def = tree.getClasses().stream().filter(cd -> cd.getFullName().equals(className)).findFirst().orElse(
                new ClassDef(new Modifier[]{Modifier.PUBLIC}, tree.getPackage(), tree.getName().toString(),
                        new QualifiedName("java", "lang", "Object"), new QualifiedName[0], new ArrayList<>())
        );

        Block b = new Block();
        Fun clinit = new Fun(new QualifiedName("<clinit>"), b, new Modifier[]{Modifier.STATIC}, null);
        // add the functions and fields to the synthetic class

        for (Statement statement : tree.getStatements()) {
            if (statement instanceof VarDecl) {
                VarDecl decl = (VarDecl) statement;
                decl.addModifier(Modifier.STATIC);
                def.getStatements().add(statement);
                b.append(new BinOp(decl.getName(), Operator.ASSIGNMENT, decl.getInitialValue()));
            } else {
                b.append(statement);
            }
            statement.setParent(def);
        }

        for (Fun fun : tree.getFunctions()) {
            fun.addModifier(Modifier.STATIC);
            def.getStatements().add(fun);
            fun.setParent(def);
        }

        def.getStatements().add(clinit);
        tree.addClass(def);

        for (ClassDef classDef : tree.getClasses()) {
            classDef.setPackage(tree.getPackage());
            if (!classDef.equals(def)) {
                // add class initializer if one does not exist
                clinit = new Fun(new QualifiedName("<clinit>"), new Block(), new Modifier[]{Modifier.STATIC}, null);
                classDef.getStatements().add(clinit);
            }
        }
    }

    private String findFile(final QualifiedName name) {
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
