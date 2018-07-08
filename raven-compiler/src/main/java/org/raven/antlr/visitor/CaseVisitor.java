package org.raven.antlr.visitor;

import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ast.*;
import org.raven.core.wrappers.TNull;

import java.util.List;

public class CaseVisitor extends RavenBaseVisitor<Case> {

    private CaseVisitor() {
    }

    @Override
    public Case visitWhenCase(final RavenParser.WhenCaseContext ctx) {
        final Expression caseExpr = ctx.expression(0).accept(ExpressionVisitor.INSTANCE);
        final Block block;
        if (ctx.block() != null) {
            block = parseBlock(ctx.block());
        } else {
            block = parseExpr(ctx.expression(1));
        }

        return new Case(caseExpr, block);
    }

    private Block parseExpr(final RavenParser.ExpressionContext ctx) {
        final Expression val = ctx.accept(ExpressionVisitor.INSTANCE);
        val.setPop(false);
        return new Block(val);
    }

    private Block parseBlock(final RavenParser.BlockContext ctx) {
        final Block block = ctx.accept(BlockVisitor.INSTANCE);

        final List<Statement> stmts = block.getStatements();
        if (stmts.isEmpty() || !(stmts.get(stmts.size() - 1) instanceof Expression)) {
            block.append(new Literal(TNull.NULL));
        } else if (!stmts.isEmpty() && (stmts.get(stmts.size() - 1) instanceof Expression)) {
            ((Expression)stmts.get(stmts.size() - 1)).setPop(false);
        }
        return block;
    }

    @Override
    public Case visitWhenElse(final RavenParser.WhenElseContext ctx) {
        final Expression caseExpr = null;
        final Block block;
        if (ctx.block() != null) {
            block = parseBlock(ctx.block());
        } else {
            block = parseExpr(ctx.expression());
        }

        return new Case(caseExpr, block);
    }

    public static final CaseVisitor INSTANCE = new CaseVisitor();
}
