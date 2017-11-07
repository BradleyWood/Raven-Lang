package org.toylang.antlr.visitor;

import org.toylang.antlr.ToyLangBaseVisitor;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.ast.Expression;
import org.toylang.antlr.ast.Return;

public class ReturnVisitor extends ToyLangBaseVisitor<Return> {

    private ReturnVisitor() {
    }

    @Override
    public Return visitReturnStatement(ToyLangParser.ReturnStatementContext ctx) {
        Expression expression = null;

        if (ctx.expression() != null)
            expression = ctx.expression().accept(ExpressionVisitor.INSTANCE);

        return new Return(expression);
    }

    public static final ReturnVisitor INSTANCE = new ReturnVisitor();
}
