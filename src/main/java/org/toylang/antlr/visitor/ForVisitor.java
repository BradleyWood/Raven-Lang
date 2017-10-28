package org.toylang.antlr.visitor;

import org.toylang.antlr.Operator;
import org.toylang.antlr.ToyLangBaseVisitor;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.ast.*;
import org.toylang.core.wrappers.TBoolean;
import org.toylang.core.wrappers.TInt;

import java.util.Arrays;

public class ForVisitor extends ToyLangBaseVisitor<For> {

    private ForVisitor() {
    }

    @Override
    public For visitForStatement(ToyLangParser.ForStatementContext ctx) {
        Block body = new Block();
        Block after = new Block();

        if (ctx.range() != null) {
            QualifiedName name = new QualifiedName(ctx.IDENTIFIER().getText());
            boolean inc = ctx.range().inc() != null;

            Range range = ctx.range().accept(RangeVisitor.INSTANCE);

            VarDecl init = new VarDecl(name, range.getStart());

            Expression condition = new BinOp(name, inc ? Operator.LT : Operator.GT, range.getEnd());

            body.append(ctx.statement().accept(StatementVisitor.INSTANCE));


            after.append(new BinOp(name, Operator.ASSIGNMENT, new BinOp(name, Operator.ADD, new Literal(new TInt(inc ? 1 : -1)))));

            return new For(init, condition, body, after);
        } else if(ctx.statement() != null) {
            Statement init = new Expression();
            Expression condition = new Literal(TBoolean.TRUE);
            if(ctx.init != null)
                init = ctx.init.accept(ExpressionVisitor.INSTANCE);
            else if(ctx.decl != null)
                init = ctx.decl.accept(VarDeclVisitor.INSTANCE);
            if(ctx.cond != null)
                condition = ctx.cond.accept(ExpressionVisitor.INSTANCE);

            int size = ctx.paramList().param() != null ? ctx.paramList().param().size() : 0;
            Expression[] af = new Expression[size];
            for(int i = 0; i < af.length; i++) {
                af[i] = ctx.paramList().param(i).accept(ExpressionVisitor.INSTANCE);
            }
            body.append(ctx.statement().accept(StatementVisitor.INSTANCE));
            Arrays.stream(af).forEach(after::append);
            return new For(init, condition, body, after);
        }
        return null;
    }

    public static final ForVisitor INSTANCE = new ForVisitor();
}
