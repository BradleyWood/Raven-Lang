package org.toylang.repl;

import org.toylang.antlr.Modifier;
import org.toylang.antlr.Operator;
import org.toylang.antlr.StatementParser;
import org.toylang.antlr.ast.*;
import org.toylang.compiler.ClassMaker;
import org.toylang.compiler.Errors;
import org.toylang.core.ByteClassLoader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Read-eval-print-loop utility
 */
public class Repl {

    private final ByteClassLoader classLoader = new ByteClassLoader(null, Repl.class.getClassLoader(), new HashMap<>());
    private final LinkedList<Import> imports = new LinkedList<>();
    private Class parent = null;
    private int counter = 0;


    public void exec(String line) {
        Class cl = build(line);
        if (cl != null) {
            try {
                parent = cl;
                Object o = cl.newInstance();
                cl.getDeclaredMethod("exec").invoke(o);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    private Class build(String line) {
        List<Statement> statementList = StatementParser.parseStatements(line);

        if (Errors.getErrorCount() > 0) {
            Errors.reset();
            return null;
        }

        LinkedList<VarDecl> staticVars = new LinkedList<>();
        LinkedList<Fun> functions = new LinkedList<>();
        LinkedList<Statement> statements = new LinkedList<>();

        for (Statement statement : statementList) {
            if (statement instanceof Import) {
                if (!imports.contains(statement))
                    imports.add((Import) statement);
            } else if (statement instanceof VarDecl) {
                VarDecl decl = (VarDecl) statement;
                decl.setModifiers(Modifier.PUBLIC);
                staticVars.add(decl);
                statements.add(new BinOp(decl.getName(), Operator.ASSIGNMENT, decl.getInitialValue()));
            } else if (statement instanceof Fun) {
                functions.add((Fun) statement);
            } else {
                statements.add(statement);
            }
        }

        QualifiedName superClass = new QualifiedName("java", "lang", "Object");
        if (parent != null) {
            superClass = new QualifiedName(parent.getName().split("\\."));
        }
        imports.add(new Import(superClass));

        String name = "Repl" + counter++;
        ClassDef def = new ClassDef(new Modifier[]{Modifier.PUBLIC}, new QualifiedName("repl"), name, superClass,
                new QualifiedName[0], new ArrayList<>());

        ClassMaker maker = new ClassMaker(def, imports.stream().map(Import::getName).collect(Collectors.toList()));
        staticVars.forEach(maker::addStaticFields);
        def.getMethods().add(createExec(statements));

        functions.forEach(maker::addStaticMethods);

        Fun clinit = new Fun(new QualifiedName("<clinit>"), new Block(), null, null);
        maker.addStaticMethods(clinit);

        maker.make();
        byte[] bytes = maker.getBytes();
        if (Errors.getErrorCount() > 0) {
            Errors.printErrors();
            return null;
        }

        String clName = "repl." + name;

        try {
            FileOutputStream fos = new FileOutputStream("target/classes/repl/"+name+".class");
            fos.write(bytes);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        classLoader.addDef(clName, bytes);
        try {
            return classLoader.loadClass(clName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Fun createExec(List<Statement> statements) {
        return new Fun(new QualifiedName("exec"),
                new Block(statements.toArray(new Statement[statements.size()])), new Modifier[]{Modifier.PUBLIC}, new String[0]);
    }
}
