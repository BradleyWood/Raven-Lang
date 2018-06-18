package org.raven.antlr.visitor;

import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ast.Expression;
import org.raven.antlr.ast.If;
import org.raven.antlr.ast.Statement;

public class IfVisitor extends RavenBaseVisitor<If> {

    private IfVisitor() {
    }

    @Override
    public If visitIfStatement(final RavenParser.IfStatementContext ctx) {
        Expression condition = ctx.expression().accept(ExpressionVisitor.INSTANCE);

        Statement body = ctx.statement().get(0).accept(StatementVisitor.INSTANCE);
        Statement else_ = null;

        if (ctx.statement().size() == 2)
            else_ = ctx.statement().get(1).accept(StatementVisitor.INSTANCE);

        If ifStatement = new If(condition, body, else_);
        String xd = ctx.getText();
        condition.setParent(ifStatement);
        body.setParent(ifStatement);

        if (else_ != null)
            else_.setParent(ifStatement);

        return ifStatement;
    }

    public static final IfVisitor INSTANCE = new IfVisitor();

}
