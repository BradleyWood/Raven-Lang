package org.raven.antlr.visitor;

import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.RavenTree;
import org.raven.antlr.ast.ClassDef;
import org.raven.antlr.ast.Fun;
import org.raven.antlr.ast.Statement;

import java.util.LinkedList;

public class RavenFileVisitor extends RavenBaseVisitor<RavenTree> {

    @Override
    public RavenTree visitRavenFile(final RavenParser.RavenFileContext ctx) {
        LinkedList<Statement> statements = new LinkedList<>();
        LinkedList<Fun> functions = new LinkedList<>();
        LinkedList<ClassDef> classes = new LinkedList<>();

        RavenTree tree = new RavenTree();

        if (ctx.packageDef() != null) {
            tree.setPackage(ctx.packageDef().qualifiedName().accept(QualifiedNameVisitor.INSTANCE));
        }
        if (ctx.importStatement() != null) {
            ctx.importStatement().forEach(imp -> tree.addImport(imp.qualifiedName().accept(QualifiedNameVisitor.INSTANCE)));
        }
        ctx.statement().forEach(stmtCtx -> {
            Statement s = stmtCtx.accept(StatementVisitor.INSTANCE);
            if (s != null) {
                if (s instanceof Fun) {
                    functions.add((Fun) s);
                } else if (s instanceof ClassDef) {
                    classes.add((ClassDef) s);
                } else {
                    statements.add(s);
                }
            }
        });

        statements.forEach(stmt -> stmt.setParent(tree));
        functions.forEach(fun -> fun.setParent(tree));
        classes.forEach(clazz -> clazz.setParent(tree));

        tree.addStatements(statements);
        tree.addFunctions(functions);
        tree.addClasses(classes);

        return tree;
    }
}