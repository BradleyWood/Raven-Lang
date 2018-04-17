package org.raven.antlr.visitor;

import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ast.DictDef;
import org.raven.antlr.ast.Expression;

public class DictDefVisitor extends RavenBaseVisitor<DictDef> {

    private DictDefVisitor() {
    }

    @Override
    public DictDef visitDict(final RavenParser.DictContext ctx) {
        int size = 0;
        if (ctx.dictParamList() != null) {
            if (ctx.dictParamList().dictParam() != null) {
                size = ctx.dictParamList().dictParam().size();
            }
        }
        Expression[] keys = new Expression[size];
        Expression[] values = new Expression[size];
        for (int i = 0; i < size; i++) {
            keys[i] = ctx.dictParamList().dictParam(i).expression(0).accept(ExpressionVisitor.INSTANCE);
            values[i] = ctx.dictParamList().dictParam(i).expression(1).accept(ExpressionVisitor.INSTANCE);
        }
        return new DictDef(keys, values);
    }

    public static final DictDefVisitor INSTANCE = new DictDefVisitor();
}
