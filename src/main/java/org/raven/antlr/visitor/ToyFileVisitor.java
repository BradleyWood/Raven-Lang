package org.raven.antlr.visitor;

import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ToyTree;
import org.raven.antlr.ast.Fun;
import org.raven.antlr.ast.Statement;

import java.util.ArrayList;

public class ToyFileVisitor extends RavenBaseVisitor<ToyTree> {

    @Override
    public ToyTree visitToyFile(RavenParser.ToyFileContext ctx) {
        ArrayList<Statement> statements = new ArrayList<>();
        ArrayList<Statement> functions = new ArrayList<>(); // functions at bottom

        ToyTree tree = new ToyTree(statements);

        if (ctx.packageDef() != null) {
            tree.setPackage(ctx.packageDef().qualifiedName().accept(QualifiedNameVisitor.INSTANCE));
        }
        if (ctx.importStatement() != null) {
            ctx.importStatement().forEach(imp -> tree.addImport(imp.qualifiedName().accept(QualifiedNameVisitor.INSTANCE)));
        }
        ctx.statement().forEach(stmtCtx -> {
            Statement s = stmtCtx.accept(StatementVisitor.INSTANCE);
            if (s != null) {
                if (s instanceof Fun)
                    functions.add(s);
                else
                    statements.add(s);
            }
        });
        statements.addAll(functions);
        return tree;
    }
}