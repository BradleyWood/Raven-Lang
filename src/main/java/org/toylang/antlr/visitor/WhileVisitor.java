package org.toylang.antlr.visitor;

import org.toylang.antlr.ToyLangBaseVisitor;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.ast.Expression;
import org.toylang.antlr.ast.Statement;
import org.toylang.antlr.ast.While;

public class WhileVisitor extends ToyLangBaseVisitor<While> {

    private WhileVisitor() {}

    @Override
    public While visitWhileStatement(ToyLangParser.WhileStatementContext ctx) {
        Expression condition = ctx.expression().accept(ExpressionVisitor.INSTANCE);
        Statement body = ctx.statement().accept(StatementVisitor.INSTANCE);

        return new While(condition, body);
    }
    public static final WhileVisitor INSTANCE = new WhileVisitor();
}
