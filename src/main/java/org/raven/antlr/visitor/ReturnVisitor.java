package org.raven.antlr.visitor;

import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ast.Expression;
import org.raven.antlr.ast.Return;

public class ReturnVisitor extends RavenBaseVisitor<Return> {

    private ReturnVisitor() {
    }

    @Override
    public Return visitReturnStatement(final RavenParser.ReturnStatementContext ctx) {
        Expression expression = null;

        if (ctx.expression() != null)
            expression = ctx.expression().accept(ExpressionVisitor.INSTANCE);

        Return ret = new Return(expression);
        if (expression != null)
            expression.setParent(ret);

        return ret;
    }

    public static final ReturnVisitor INSTANCE = new ReturnVisitor();
}
