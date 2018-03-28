package org.raven.antlr.visitor;

import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ast.Expression;
import org.raven.antlr.ast.Raise;

public class RaiseVisitor extends RavenBaseVisitor<Raise> {

    public RaiseVisitor() {
    }

    @Override
    public Raise visitRaiseStatement(RavenParser.RaiseStatementContext ctx) {
        Expression expression = ctx.expression().accept(ExpressionVisitor.INSTANCE);
        return new Raise(expression);
    }

    public static final RaiseVisitor INSTANCE = new RaiseVisitor();
}
