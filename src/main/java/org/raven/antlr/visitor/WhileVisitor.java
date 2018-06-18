package org.raven.antlr.visitor;

import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ast.Expression;
import org.raven.antlr.ast.Statement;
import org.raven.antlr.ast.While;

public class WhileVisitor extends RavenBaseVisitor<While> {

    private WhileVisitor() {
    }

    @Override
    public While visitWhileStatement(final RavenParser.WhileStatementContext ctx) {
        Expression condition = ctx.expression().accept(ExpressionVisitor.INSTANCE);
        Statement body = ctx.statement().accept(StatementVisitor.INSTANCE);

        While whileStatement = new While(condition, body, ctx.DO() != null);

        condition.setParent(whileStatement);
        body.setParent(whileStatement);

        return whileStatement;
    }

    public static final WhileVisitor INSTANCE = new WhileVisitor();
}
