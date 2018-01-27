package org.toylang.antlr.visitor;

import org.toylang.antlr.ToyLangBaseVisitor;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.ast.*;

public class ListIndexVisitor extends ToyLangBaseVisitor<Expression> {

    private ListIndexVisitor() {

    }

    @Override
    public Expression visitListIdx(ToyLangParser.ListIdxContext ctx) {
        QualifiedName lst = null;
        Expression call = null;
        if (ctx.qualifiedName() != null) {
            lst = ctx.qualifiedName().accept(QualifiedNameVisitor.INSTANCE);
        } else if (ctx.funCall() != null) {
            call = ctx.funCall().accept(FunCallVisitor.INSTANCE);
        }

        Expression[] ia = new Expression[ctx.expression().size()];
        for (int i = 0; i < ia.length; i++) {
            ia[i] = ctx.expression(i).accept(ExpressionVisitor.INSTANCE);
        }
        if (call != null) {
            return new ExpressionGroup(call, new ListIndex(null, ia));
        }
        return new ListIndex(lst, ia);
    }

    public static final ListIndexVisitor INSTANCE = new ListIndexVisitor();
}
