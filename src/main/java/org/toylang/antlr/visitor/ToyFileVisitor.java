package org.toylang.antlr.visitor;

import org.toylang.antlr.ToyLangBaseVisitor;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.ToyTree;
import org.toylang.antlr.ast.Fun;
import org.toylang.antlr.ast.Statement;

import java.util.ArrayList;

public class ToyFileVisitor extends ToyLangBaseVisitor<ToyTree> {

    @Override
    public ToyTree visitToyFile(ToyLangParser.ToyFileContext ctx) {
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