package org.raven.repl;

import org.raven.antlr.Modifier;
import org.raven.antlr.Operator;
import org.raven.antlr.StatementParser;
import org.raven.antlr.RavenTree;
import org.raven.antlr.ast.*;
import org.raven.compiler.ClassMaker;
import org.raven.compiler.SymbolMap;
import org.raven.core.ByteClassLoader;
import org.raven.core.ExceptionHandler;
import org.raven.core.wrappers.TVoid;
import org.raven.error.Errors;
import org.raven.util.Settings;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Read-eval-print-loop utility
 */
public class Repl {

    private static final ExceptionHandler exceptionHandler = new ExceptionHandler();
    private final ByteClassLoader classLoader = new ByteClassLoader(null, Repl.class.getClassLoader(), new HashMap<>());
    private final LinkedList<Import> imports = new LinkedList<>();
    private Class parent = null;
    private int counter = 0;

    private static int instanceCount = 0;
    private int id;

    public Repl() {
        id = instanceCount++;
    }

    public void exec(String line) {
        Settings.set("REPL", true);
        Thread.UncaughtExceptionHandler currentHandler = Thread.getDefaultUncaughtExceptionHandler();
        try {
            Class<?> cl = build(line);
            if (cl != null) {
                Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
                Object obj = cl.getDeclaredMethod("exec").invoke(null);
                if (!TVoid.VOID.equals(obj))
                    System.out.println(obj);
                parent = cl;
            }
        } catch (InvocationTargetException e) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e.getCause());
        } catch (VerifyError | NoSuchMethodException | IllegalAccessException e) {
            System.err.println("REPL INTERNAL ERROR");
        } finally {
            Thread.setDefaultUncaughtExceptionHandler(currentHandler);
        }
        Settings.set("REPL", false);
    }

    private Class build(String line) {
        List<Statement> statementList = StatementParser.parseStatements(line);

        if (Errors.getErrorCount() > 0) {
            Errors.printErrors();
            Errors.reset();
            return null;
        }

        LinkedList<VarDecl> variables = new LinkedList<>();
        LinkedList<Fun> functions = new LinkedList<>();
        LinkedList<Statement> statements = new LinkedList<>();

        for (Statement statement : statementList) {
            if (statement instanceof Import) {
                if (!imports.contains(statement)) {
                    try {
                        Class clazz = Class.forName(((Import) statement).getName().toString());
                        SymbolMap.map(clazz);
                        imports.add((Import) statement);
                    } catch (ClassNotFoundException e) {
                        System.err.println("Class not found: " + statement);
                    }
                }
            } else if (statement instanceof VarDecl) {
                VarDecl decl = (VarDecl) statement;
                decl.getModifiers().clear();
                decl.addModifier(Modifier.PUBLIC);
                decl.addModifier(Modifier.STATIC);
                variables.add(decl);
                statements.add(new BinOp(decl.getName(), Operator.ASSIGNMENT, decl.getInitialValue()));
            } else if (statement instanceof Fun) {
                Fun fun = (Fun) statement;
                fun.getModifiers().clear();
                fun.addModifier(Modifier.PUBLIC);
                fun.addModifier(Modifier.STATIC);
                functions.add((Fun) statement);
            } else {
                statements.add(statement);
            }
        }

        if (!statements.isEmpty()) {
            Statement stmt = statements.removeLast();
            if (stmt instanceof BinOp && ((BinOp) stmt).getOp() != Operator.ASSIGNMENT ||
                    (stmt instanceof Expression && !(stmt instanceof BinOp))) {
                Expression expr = (Expression) stmt;
                expr.setPop(false);
                stmt = new Return(expr);
            }
            statements.addLast(stmt);
        }

        QualifiedName superClass = new QualifiedName("java", "lang", "Object");
        if (parent != null) {
            superClass = new QualifiedName(parent.getName().split("\\."));
        }
        imports.add(new Import(superClass));

        String name = "Repl" + id + "_" + counter++;
        ClassDef def = new ClassDef(new Modifier[]{Modifier.PUBLIC}, new QualifiedName("repl"), name, superClass,
                new QualifiedName[0], new ArrayList<>());

        RavenTree tree = new RavenTree(Collections.singletonList(def));
        tree.setSourceFile("<stdin>");
        def.setSourceTree(tree);

        ClassMaker maker = new ClassMaker(def, imports.stream().map(Import::getName).collect(Collectors.toList()));
        def.getStatements().addAll(variables);
        def.getStatements().addAll(functions);
        def.getStatements().add(createExec(statements));

        Fun clinit = new Fun(new QualifiedName("<clinit>"), new Block(), new Modifier[]{Modifier.STATIC}, null);
        def.getStatements().add(clinit);
        SymbolMap.map(def);
        maker.make();
        byte[] bytes = maker.getBytes();
        if (Errors.getErrorCount() > 0) {
            Errors.printErrors();
            Errors.reset();
            return null;
        }

        String clName = "repl." + name;

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
                new Block(statements.toArray(new Statement[statements.size()])), new Modifier[]{Modifier.PUBLIC, Modifier.STATIC}, new String[0]);
    }
}
