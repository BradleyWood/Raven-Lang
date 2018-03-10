package org.raven.antlr.visitor;

import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ast.Call;
import org.raven.antlr.ast.Expression;
import org.raven.antlr.ast.Go;
import org.raven.antlr.ast.QualifiedName;

public class GoVisitor extends RavenBaseVisitor<Go> {

    private GoVisitor() {
    }

    @Override
    public Go visitGoStatement(RavenParser.GoStatementContext ctx) {
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
