package org.toylang.antlr.visitor;

import org.toylang.antlr.Operator;
import org.toylang.antlr.ToyLangBaseVisitor;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.ast.*;
import org.toylang.core.ToyBoolean;
import org.toylang.core.ToyInt;
import org.toylang.core.ToyNull;
import org.toylang.core.ToyObject;

import java.util.Arrays;

public class ForVisitor extends ToyLangBaseVisitor<Block> {

    private ForVisitor() {
    }

    @Override
    public Block visitForStatement(ToyLangParser.ForStatementContext ctx) {
        Block block = new Block();
        Block body = new Block();

        if (ctx.range() != null) {
            QualifiedName name = new QualifiedName(ctx.IDENTIFIER().getText());
            boolean inc = ctx.range().inc() != null;

            Range range = ctx.range().accept(RangeVisitor.INSTANCE);

            block.append(new VarDecl(name, range.getStart()));

            Expression condition = new BinOp(name, inc ? Operator.LT : Operator.GT, range.getEnd());

            body.append(ctx.statement().accept(StatementVisitor.INSTANCE));


            body.append(new BinOp(name, Operator.ASSIGNMENT, new BinOp(name, Operator.ADD, new Literal(new ToyInt(inc ? 1 : -1)))));

            While loop = new While(condition, body);
            block.append(loop);

        } else if(ctx.statement() != null) {
            Statement init = new Expression();
            Expression condition = new Literal(ToyBoolean.TRUE);
            if(ctx.init != null)
                init = ctx.init.accept(ExpressionVisitor.INSTANCE);
            else if(ctx.decl != null)
                init = ctx.decl.accept(VarDeclVisitor.INSTANCE);
            if(ctx.cond != null)
                condition = ctx.cond.accept(ExpressionVisitor.INSTANCE);
            block.append(init);

            int size = ctx.paramList().param() != null ? ctx.paramList().param().size() : 0;
            Expression[] after = new Expression[size];
            for(int i = 0; i < after.length; i++) {
                after[i] = ctx.paramList().param(i).accept(ExpressionVisitor.INSTANCE);
            }
            body.append(ctx.statement().accept(StatementVisitor.INSTANCE));
            Arrays.stream(after).forEach(body::append);
            While loop = new While(condition, body);
            block.append(loop);
        }
        return block;
    }

    public static final ForVisitor INSTANCE = new ForVisitor();
}
