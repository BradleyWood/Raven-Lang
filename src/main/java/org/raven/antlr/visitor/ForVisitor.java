package org.raven.antlr.visitor;

import org.raven.antlr.Operator;
import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ast.*;
import org.raven.core.wrappers.TBoolean;
import org.raven.core.wrappers.TInt;

import java.util.Arrays;

public class ForVisitor extends RavenBaseVisitor<For> {

    private static int count = 0;

    private ForVisitor() {
    }

    @Override
    public For visitForStatement(final RavenParser.ForStatementContext ctx) {
        Block body = new Block();
        Block after = new Block();
        Statement init;
        Expression condition;

        RavenParser.ForControlContext forControl = ctx.forControl();

        while (forControl.forControl() != null) {
            forControl = forControl.forControl();
        }

        if (forControl.COLON() != null) {
            // foreach
            QualifiedName placeholderName = new QualifiedName(String.valueOf(++count));
            init = new VarDecl(placeholderName, new Literal(new TInt(0)));
            QualifiedName name = new QualifiedName(forControl.IDENTIFIER().getText());
            body.append(new VarDecl(name, null));

            Expression iterable = forControl.expression(0).accept(ExpressionVisitor.INSTANCE);
            QualifiedName iterableName;

            if (!(iterable instanceof QualifiedName)) {
                iterableName = new QualifiedName("iterator_"+String.valueOf(count));
                VarDecl decl = new VarDecl(iterableName, iterable);
                body.append(decl);
            } else {
                iterableName = (QualifiedName) iterable;
            }

            condition = new BinOp(placeholderName, Operator.LT, new Call(new QualifiedName("len"), iterable));
            body.append(new BinOp(name, Operator.ASSIGNMENT, new ListIndex(iterableName, placeholderName)));
            body.append(ctx.statement().accept(StatementVisitor.INSTANCE));
            after.append(new BinOp(placeholderName, Operator.ASSIGNMENT, new BinOp(null, Operator.INC, placeholderName)));
        } else if (forControl.range() != null) {
            // for i in range a..b
            QualifiedName name = new QualifiedName(forControl.IDENTIFIER().getText());
            boolean inc = forControl.range().inc() != null;

            Range range = forControl.range().accept(RangeVisitor.INSTANCE);

            init = new VarDecl(name, range.getStart());

            condition = new BinOp(name, inc ? Operator.LT : Operator.GT, range.getEnd());

            body.append(ctx.statement().accept(StatementVisitor.INSTANCE));

            after.append(new BinOp(name, Operator.ASSIGNMENT, new BinOp(null, inc ? Operator.INC : Operator.DEC, name)));
        } else if (ctx.statement() != null) {
            // for (init; condition; after...)
            init = new Block();
            condition = new Literal(TBoolean.TRUE);
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
        } else {
            return null;
        }

        For forLoop = new For(init, condition, body, after);

        init.setParent(forLoop);
        condition.setParent(forLoop);
        body.setParent(forLoop);
        after.setParent(forLoop);

        return forLoop;
    }

    public static final ForVisitor INSTANCE = new ForVisitor();
}