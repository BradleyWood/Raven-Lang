package org.raven.antlr.visitor;

import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ast.Range;

public class RangeVisitor extends RavenBaseVisitor<Range> {

    private RangeVisitor() {
    }

    @Override
    public Range visitRange(RavenParser.RangeContext ctx) {
        return new Range(ctx.expression(0).accept(ExpressionVisitor.INSTANCE), ctx.expression(1).accept(ExpressionVisitor.INSTANCE));
    }

    public static final RangeVisitor INSTANCE = new RangeVisitor();
}
