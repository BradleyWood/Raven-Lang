package org.toylang.antlr.visitor;

import org.toylang.antlr.ToyLangBaseVisitor;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.ast.Range;

public class RangeVisitor extends ToyLangBaseVisitor<Range> {

    private RangeVisitor() {
    }

    @Override
    public Range visitRange(ToyLangParser.RangeContext ctx) {
        return new Range(ctx.expression(0).accept(ExpressionVisitor.INSTANCE), ctx.expression(1).accept(ExpressionVisitor.INSTANCE));
    }

    public static final RangeVisitor INSTANCE = new RangeVisitor();
}
