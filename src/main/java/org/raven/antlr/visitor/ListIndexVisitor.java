package org.raven.antlr.visitor;

import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ast.*;

import java.util.Arrays;

public class ListIndexVisitor extends RavenBaseVisitor<ListIndex> {

    private ListIndexVisitor() {

    }

    @Override
    public ListIndex visitListIdx(final RavenParser.ListIdxContext ctx) {
        Expression preceding = null;

        Expression[] ia = new Expression[ctx.expression().size()];
        for (int i = 0; i < ia.length; i++) {
            ia[i] = ctx.expression(i).accept(ExpressionVisitor.INSTANCE);
        }

        ListIndex listIndex = new ListIndex(preceding, ia);
        Arrays.stream(ia).forEach(e -> e.setParent(listIndex));

        return listIndex;
    }

    public static final ListIndexVisitor INSTANCE = new ListIndexVisitor();
}
