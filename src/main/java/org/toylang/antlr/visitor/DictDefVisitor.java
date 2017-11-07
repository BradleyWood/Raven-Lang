package org.toylang.antlr.visitor;

import org.toylang.antlr.ToyLangBaseVisitor;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.ast.DictDef;
import org.toylang.antlr.ast.Expression;

public class DictDefVisitor extends ToyLangBaseVisitor<DictDef> {

    private DictDefVisitor() {
    }

    @Override
    public DictDef visitDict(ToyLangParser.DictContext ctx) {
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
