package org.toylang.antlr.visitor;

import org.toylang.antlr.ToyLangBaseVisitor;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.ast.Expression;
import org.toylang.antlr.ast.ListDef;

public class ArrayDefVisitor extends ToyLangBaseVisitor<ListDef> {

    private ArrayDefVisitor() {}
    @Override
    public ListDef visitList(ToyLangParser.ListContext ctx) {
        if(ctx.paramList() != null) {
            Expression[] expra = new Expression[ctx.paramList().param().size()];
            for(int i = 0; i < expra.length; i++) {
                expra[i] = ctx.paramList().param(i).accept(ExpressionVisitor.INSTANCE);
            }
            return new ListDef(expra);
        }
        return new ListDef();
    }
    public static ArrayDefVisitor INSTANCE = new ArrayDefVisitor();
}
