package org.toylang.antlr.visitor;

import org.toylang.antlr.ToyLangBaseVisitor;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.ast.Call;
import org.toylang.antlr.ast.Expression;
import org.toylang.antlr.ast.QualifiedName;

public class FunCallVisitor extends ToyLangBaseVisitor<Expression> {

    private FunCallVisitor() {
    }

    @Override
    public Expression visitFunCall(ToyLangParser.FunCallContext ctx) {
        QualifiedName name;
        if (ctx.SUPER() != null) {
            name = new QualifiedName("super");
        } else if (ctx.THIS() != null) {
            name = new QualifiedName("this");
        } else {
            name = new QualifiedName(ctx.IDENTIFIER().getText());
        }
        Expression[] expressions = new Expression[0];
        if (ctx.paramList() != null)
            expressions = new Expression[ctx.paramList().param().size()];
        for (int i = 0; i < expressions.length; i++) {
            expressions[i] = ctx.paramList().param(i).accept(ExpressionVisitor.INSTANCE);
        }
        return new Call(name, expressions);
    }

    public static FunCallVisitor INSTANCE = new FunCallVisitor();
}
