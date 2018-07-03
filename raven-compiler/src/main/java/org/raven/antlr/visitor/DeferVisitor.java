package org.raven.antlr.visitor;

import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ast.Call;
import org.raven.antlr.ast.Defer;

public class DeferVisitor extends RavenBaseVisitor<Defer> {

    private DeferVisitor() {
    }

    @Override
    public Defer visitDeferStatement(final RavenParser.DeferStatementContext ctx) {
        final Call call = (Call) ctx.funCall().accept(FunCallVisitor.INSTANCE);
        final Defer defer = new Defer(call);

        if (ctx.expression() != null)
            call.setPrecedingExpr(ctx.expression().accept(ExpressionVisitor.INSTANCE));
        call.setParent(defer);

        return defer;
    }

    public static final DeferVisitor INSTANCE = new DeferVisitor();
}
