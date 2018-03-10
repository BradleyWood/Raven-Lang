package org.raven.antlr.visitor;

import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ast.*;

public class ListIndexVisitor extends RavenBaseVisitor<ListIndex> {

    private ListIndexVisitor() {

    }

    @Override
    public ListIndex visitListIdx(RavenParser.ListIdxContext ctx) {
        Expression preceding = null;

//        if (ctx.qualifiedName() != null) {
//            preceding = ctx.qualifiedName().accept(QualifiedNameVisitor.INSTANCE);
//        } else if (ctx.list() != null) {
//            preceding = ctx.list().accept(ArrayDefVisitor.INSTANCE);
//        } else if (ctx.dict() != null) {
//            preceding = ctx.dict().accept(DictDefVisitor.INSTANCE);
//        } else if (ctx.funCall() != null) {
//            preceding = ctx.funCall().accept(FunCallVisitor.INSTANCE);
//        }

        Expression[] ia = new Expression[ctx.expression().size()];
        for (int i = 0; i < ia.length; i++) {
            ia[i] = ctx.expression(i).accept(ExpressionVisitor.INSTANCE);
        }
        return new ListIndex(preceding, ia);
    }

    public static final ListIndexVisitor INSTANCE = new ListIndexVisitor();
}
