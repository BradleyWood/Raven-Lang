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

    private static int instanceCount = 0;
    private int id;

    public Repl(boolean debug) {
        this.debug = debug;
        id = instanceCount++;
    }

    public Repl() {
        this(false);
    }

    public void exec(String line) {
        try {
            Class<?> cl = build(line);
            if (cl != null) {
                parent = cl;
                cl.getDeclaredMethod("exec").invoke(null);
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

        LinkedList<VarDecl> variables = new LinkedList<>();
        LinkedList<Fun> functions = new LinkedList<>();
        LinkedList<Statement> statements = new LinkedList<>();

        for (Statement statement : statementList) {
            if (statement instanceof Import) {
                if (!imports.contains(statement))
                    imports.add((Import) statement);
            } else if (statement instanceof VarDecl) {
                VarDecl decl = (VarDecl) statement;
                decl.addModifier(Modifier.PUBLIC);
                decl.addModifier(Modifier.STATIC);
                variables.add(decl);
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
        def.getStatements().addAll(variables);
        def.getStatements().addAll(functions);
        def.getStatements().add(createExec(statements));

        Fun clinit = new Fun(new QualifiedName("<clinit>"), new Block(), new Modifier[]{Modifier.STATIC}, null);
        def.getStatements().add(clinit);

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

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isDebugMode() {
        return debug;
    }
}
