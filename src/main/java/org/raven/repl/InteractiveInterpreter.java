package org.raven.repl;

import org.raven.antlr.Modifier;
import org.raven.antlr.Operator;
import org.raven.antlr.StatementParser;
import org.raven.antlr.RavenTree;
import org.raven.antlr.ast.*;
import org.raven.compiler.ClassMaker;
import org.raven.compiler.SymbolMap;
import org.raven.core.Adaptor;
import org.raven.core.ByteClassLoader;
import org.raven.core.ExceptionHandler;
import org.raven.core.wrappers.TObject;
import org.raven.core.wrappers.TVoid;
import org.raven.error.Errors;
import org.raven.util.Settings;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Read-eval-print-loop utility
 */
public class InteractiveInterpreter {

    private static final ExceptionHandler exceptionHandler = new ExceptionHandler();
    private final ByteClassLoader classLoader = new ByteClassLoader(InteractiveInterpreter.class.getClassLoader(), new HashMap<>());
    private final LinkedList<Import> imports = new LinkedList<>();
    private String lastLine = null;
    private Class parent = null;
    private int counter = 0;

    private static int instanceCount = 0;
    private int id;

    public InteractiveInterpreter() {
        id = instanceCount++;
    }

    public void exec(final String input) {
        TObject result = eval(input);
        if (result != null && !TVoid.VOID.equals(result))
            System.out.println(result);
    }

    /**
     * Evaluate a script or expression and get the result
     *
     * @param input The input source code
     * @return The result of expression or the result of the last statement in the script. Returns null on error.
     */
    public TObject eval(final String input) {
        if (input == null || input.isEmpty())
            return null;

        if (input.equals(lastLine)) {
            return eval(parent);
        }

        lastLine = input;

        try {
            Class<?> cl = build(input);
            return eval(cl);
        } catch (VerifyError e) {
            System.err.println("REPL INTERNAL ERROR");
            lastLine = null;
        }
        return null;
    }

    public <T, R> Function<T, R> getFunction(final String methodName, final Class<R> returnType)
            throws Throwable {
        if (parent == null)
            throw new NoSuchMethodException(methodName);

        return Adaptor.getFunction(parent.getDeclaredMethod(methodName, TObject.class), returnType);
    }

    private TObject eval(final Class<?> clazz) {
        Settings.set("REPL", true);
        Thread.UncaughtExceptionHandler currentHandler = Thread.getDefaultUncaughtExceptionHandler();
        try {
            if (clazz != null) {
                Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
                Object obj = clazz.getDeclaredMethod("exec").invoke(null);
                parent = clazz;
                return (TObject) obj;
            }
        } catch (InvocationTargetException e) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e.getCause());
            lastLine = null;
        } catch (NoSuchMethodException | IllegalAccessException e) {
            System.err.println("REPL INTERNAL ERROR");
            lastLine = null;
        } finally {
            Thread.setDefaultUncaughtExceptionHandler(currentHandler);
            Settings.set("REPL", false);
        }
        return null;
    }

    private Class build(final String line) {
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

        String name = "InteractiveInterpreter" + id + "_" + counter++;
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

    private static Fun createExec(final List<Statement> statements) {
        return new Fun(new QualifiedName("exec"),
                new Block(statements.toArray(new Statement[statements.size()])), new Modifier[]{Modifier.PUBLIC, Modifier.STATIC}, new String[0]);
    }
}
