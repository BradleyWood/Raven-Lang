package org.toylang.antlr.visitor;

import org.toylang.antlr.ToyLangBaseVisitor;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.ast.Call;
import org.toylang.antlr.ast.Expression;
import org.toylang.antlr.ast.Go;
import org.toylang.antlr.ast.QualifiedName;

public class GoVisitor extends ToyLangBaseVisitor<Go> {

    private GoVisitor() {
    }

    @Override
    public Go visitGoStatement(ToyLangParser.GoStatementContext ctx) {
        QualifiedName name = new QualifiedName(ctx.funCall().IDENTIFIER().getText());
        Expression[] expressions = new Expression[0];
        if (ctx.funCall().paramList() != null)
            expressions = new Expression[ctx.funCall().paramList().param().size()];
        for (int i = 0; i < expressions.length; i++) {
            expressions[i] = ctx.funCall().paramList().param(i).accept(ExpressionVisitor.INSTANCE);
        }
        Call call = new Call(name, expressions);

        if (ctx.expression() != null) {
            Expression expr = ctx.expression().accept(ExpressionVisitor.INSTANCE);
            call.setPrecedingExpr(expr);
        }

        call.setLineNumber(ctx.funCall().start.getLine());
        return new Go(call);
    }

    public static GoVisitor INSTANCE = new GoVisitor();
}
