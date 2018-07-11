package org.raven.antlr.visitor;

import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ast.Block;
import org.raven.antlr.ast.Case;
import org.raven.antlr.ast.Expression;
import org.raven.antlr.ast.When;

public class WhenVisitor extends RavenBaseVisitor<When> {

    private WhenVisitor() {
    }

    @Override
    public When visitWhenExpression(final RavenParser.WhenExpressionContext ctx) {
        Expression conditon = null;

        if (ctx.expression() != null)
            conditon = ctx.expression().accept(ExpressionVisitor.INSTANCE);

        final Case[] cases = ctx.whenCase().stream().map(c -> c.accept(CaseVisitor.INSTANCE)).toArray(Case[]::new);
        final Block elseBlock = ctx.whenElse().accept(CaseVisitor.INSTANCE).getBlock();

        final When when = new When(conditon, cases, elseBlock);

        if (conditon != null)
            conditon.setParent(when);

        elseBlock.setParent(when);

        return when;
    }

    public static final WhenVisitor INSTANCE = new WhenVisitor();
}
