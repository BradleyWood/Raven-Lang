package org.raven.antlr.visitor;

import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ast.Expression;
import org.raven.antlr.ast.ListDef;

import java.util.Arrays;

public class ArrayDefVisitor extends RavenBaseVisitor<ListDef> {

    private ArrayDefVisitor() {
    }

    @Override
    public ListDef visitList(final RavenParser.ListContext ctx) {
        if (ctx.paramList() != null) {
            Expression[] expra = new Expression[ctx.paramList().param().size()];
            for (int i = 0; i < expra.length; i++) {
                expra[i] = ctx.paramList().param(i).accept(ExpressionVisitor.INSTANCE);
            }

            ListDef def = new ListDef(expra);
            Arrays.stream(expra).forEach(e -> e.setParent(def));

            return def;
        }
        return new ListDef();
    }

    public static ArrayDefVisitor INSTANCE = new ArrayDefVisitor();
}
