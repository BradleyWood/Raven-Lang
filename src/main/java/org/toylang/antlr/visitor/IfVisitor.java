package org.toylang.antlr.visitor;

import org.toylang.antlr.ToyLangBaseVisitor;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.ast.Expression;
import org.toylang.antlr.ast.If;
import org.toylang.antlr.ast.Statement;

public class IfVisitor extends ToyLangBaseVisitor<If> {

    private IfVisitor() {}

    @Override
    public If visitIfStatement(ToyLangParser.IfStatementContext ctx) {
        Expression condition = ctx.expression().accept(ExpressionVisitor.INSTANCE);

        Statement body = ctx.statement().get(0).accept(StatementVisitor.INSTANCE);
        Statement else_ = null;

        if(ctx.statement().size() == 2)
            else_ = ctx.statement().get(1).accept(StatementVisitor.INSTANCE);

        return new If(condition,body,else_);
    }

    public static final IfVisitor INSTANCE = new IfVisitor();

}
