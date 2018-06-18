package org.raven.antlr.visitor;

import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ast.Expression;
import org.raven.antlr.ast.Range;

public class RangeVisitor extends RavenBaseVisitor<Range> {

    private RangeVisitor() {
    }

    @Override
    public Range visitRange(final RavenParser.RangeContext ctx) {
        Expression low = ctx.expression(0).accept(ExpressionVisitor.INSTANCE);
        Expression high = ctx.expression(1).accept(ExpressionVisitor.INSTANCE);

        Range range = new Range(low, high);
        low.setParent(range);
        high.setParent(range);

        return range;
    }

    public static final RangeVisitor INSTANCE = new RangeVisitor();
}
