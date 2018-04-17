package org.raven.antlr.visitor;

import org.raven.antlr.Operator;
import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ast.*;
import org.raven.core.wrappers.TBoolean;
import org.raven.core.wrappers.TInt;

import java.util.Arrays;
import java.util.Random;

public class ForVisitor extends RavenBaseVisitor<For> {

    private ForVisitor() {
    }

    @Override
    public For visitForStatement(final RavenParser.ForStatementContext ctx) {
        Block body = new Block();
        Block after = new Block();

        RavenParser.ForControlContext forControl = ctx.forControl();

        while (forControl.forControl() != null) {
            forControl = forControl.forControl();
        }

        if (forControl.COLON() != null) {
            Random r = new Random();
            VarDecl init = new VarDecl(new QualifiedName(String.valueOf(r.nextInt())), new Literal(new TInt(0)));
            QualifiedName name = new QualifiedName(forControl.IDENTIFIER().getText());
            body.append(new VarDecl(name, null));

            Expression iterable = forControl.expression(0).accept(ExpressionVisitor.INSTANCE);
            QualifiedName iterableName;
            if (!(iterable instanceof QualifiedName)) {
                iterableName = new QualifiedName(String.valueOf(r.nextInt()));
                VarDecl decl = new VarDecl(iterableName, iterable);
                body.append(decl);
            } else {
                iterableName = (QualifiedName) iterable;
            }
            Expression condition = new BinOp(init.getName(), Operator.LT, new Call(new QualifiedName("len"), iterable));
            body.append(new BinOp(name, Operator.ASSIGNMENT, new ListIndex(iterableName, init.getName())));
            body.append(ctx.statement().accept(StatementVisitor.INSTANCE));
            after.append(new BinOp(init.getName(), Operator.ASSIGNMENT, new BinOp(null, Operator.INC, init.getName())));

            return new For(init, condition, body, after);
        } else if (forControl.range() != null) {
            QualifiedName name = new QualifiedName(forControl.IDENTIFIER().getText());
            boolean inc = forControl.range().inc() != null;

            Range range = forControl.range().accept(RangeVisitor.INSTANCE);

            VarDecl init = new VarDecl(name, range.getStart());

            Expression condition = new BinOp(name, inc ? Operator.LT : Operator.GT, range.getEnd());

            body.append(ctx.statement().accept(StatementVisitor.INSTANCE));


            after.append(new BinOp(name, Operator.ASSIGNMENT, new BinOp(null, inc ? Operator.INC : Operator.DEC, name)));

            return new For(init, condition, body, after);
        } else if (ctx.statement() != null) {
            Statement init = new Block();
            Expression condition = new Literal(TBoolean.TRUE);
            if (forControl.init != null)
                init = forControl.init.accept(ExpressionVisitor.INSTANCE);
            else if (forControl.decl != null)
                init = forControl.decl.accept(VarDeclVisitor.INSTANCE);
            if (forControl.cond != null)
                condition = forControl.cond.accept(ExpressionVisitor.INSTANCE);

            int size = forControl.paramList() != null ? forControl.paramList().param() != null ? forControl.paramList().param().size() : 0 : 0;
            Expression[] af = new Expression[size];
            for (int i = 0; i < af.length; i++) {
                af[i] = forControl.paramList().param(i).accept(ExpressionVisitor.INSTANCE);
            }
            body.append(ctx.statement().accept(StatementVisitor.INSTANCE));
            Arrays.stream(af).forEach(after::append);
            return new For(init, condition, body, after);
        }
        return null;
    }

    public static final ForVisitor INSTANCE = new ForVisitor();
}