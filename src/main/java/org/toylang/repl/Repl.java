package org.toylang.repl;

import org.toylang.antlr.Modifier;
import org.toylang.antlr.Operator;
import org.toylang.antlr.StatementParser;
import org.toylang.antlr.ast.*;
import org.toylang.compiler.ClassMaker;
import org.toylang.compiler.Errors;
import org.toylang.core.ByteClassLoader;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Read-eval-print-loop utility
 */
public class Repl {

    private final ByteClassLoader classLoader = new ByteClassLoader(null, Repl.class.getClassLoader(), new HashMap<>());
    private final LinkedList<Import> imports = new LinkedList<>();
    private Class parent = null;
    private int counter = 0;
    private boolean debug;

    private int id;

    public Repl(boolean debug) {
        this.debug = debug;
        Random r = new Random();
        id = r.nextInt();
    }

    public Repl() {
        this(false);
    }

    public void exec(String line) {
        try {
            Class<?> cl = build(line);
            if (cl != null) {
                parent = cl;
                Object o = cl.newInstance();
                cl.getDeclaredMethod("exec").invoke(o);
            }
        } catch (Throwable e) {
            if (debug) {
                e.printStackTrace();
            } else {
                System.err.println(e.getClass().getName() + " : " + e.getMessage());
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

        String name = "Repl" + id + "_" + counter++;
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

        classLoader.addDef(clName, bytes);
        try {
            return classLoader.loadClass(clName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Errors.reset();
        return null;
    }

    private static Fun createExec(List<Statement> statements) {
        return new Fun(new QualifiedName("exec"),
                new Block(statements.toArray(new Statement[statements.size()])), new Modifier[]{Modifier.PUBLIC}, new String[0]);
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isDebugMode() {
        return debug;
    }
}