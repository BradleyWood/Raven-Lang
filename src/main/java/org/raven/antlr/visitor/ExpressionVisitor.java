package org.raven.antlr.visitor;

import org.raven.antlr.Operator;
import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ast.*;
import org.raven.error.Errors;
import org.raven.core.wrappers.*;

public class ExpressionVisitor extends RavenBaseVisitor<Expression> {

    private ExpressionVisitor() {
    }

    @Override
    public Expression visitExpression(final RavenParser.ExpressionContext ctx) {
        Expression expr = null;

        if (ctx.expression().size() == 1 && ctx.qualifiedName() != null) {
            QualifiedName qn = ctx.qualifiedName().accept(QualifiedNameVisitor.INSTANCE);
            expr = ctx.expression(0).accept(ExpressionVisitor.INSTANCE);
            for (String s : qn.getNames()) {
                expr = new Call(expr, new QualifiedName("getField"), new QualifiedName(s));
            }
        } else if (ctx.expression().size() == 1 && ctx.funCall() != null) {
            expr = ctx.funCall().accept(FunCallVisitor.INSTANCE);
            ((Call) expr).setPrecedingExpr(ctx.expression(0).accept(ExpressionVisitor.INSTANCE));
        } else if (ctx.literal() != null) {
            expr = ctx.literal().accept(LiteralVisitor.INSTANCE);
        } else if (ctx.goExpression() != null) {
            expr = ctx.goExpression().accept(GoVisitor.INSTANCE);
        } else if (ctx.dict() != null) {
            expr = ctx.dict().accept(DictDefVisitor.INSTANCE);
        } else if (ctx.list() != null) {
            expr = ctx.list().accept(ArrayDefVisitor.INSTANCE);
        } else if (ctx.lst != null) {
            expr = getSlice(ctx);
        } else if (ctx.funCall() != null) {
            expr = ctx.funCall().accept(FunCallVisitor.INSTANCE);
        } else if (ctx.qualifiedName() != null) {
            expr = ctx.qualifiedName().accept(QualifiedNameVisitor.INSTANCE);
        } else if (ctx.varAssignment() != null) {
            expr = ctx.varAssignment().accept(AssignmentVisitor.INSTANCE);
        } else if (ctx.lhs != null && ctx.rhs != null && ctx.ASSIGNMENT() != null) {
            ListIndex lIdx = ctx.listIdx().accept(ListIndexVisitor.INSTANCE);
            lIdx.setPrecedingExpr(ctx.lhs.accept(ExpressionVisitor.INSTANCE));
            expr = new BinOp(lIdx, Operator.ASSIGNMENT, ctx.rhs.accept(ExpressionVisitor.INSTANCE));
        } else if (ctx.listIdx() != null) {
            Expression preceding = ctx.expression(0).accept(ExpressionVisitor.INSTANCE);
            ListIndex lIdx = ctx.listIdx().accept(ListIndexVisitor.INSTANCE);
            lIdx.setPrecedingExpr(preceding);
            expr = lIdx;
        } else if (ctx.expression().size() == 1) {
            expr = ctx.expression(0).accept(ExpressionVisitor.INSTANCE);
            if (ctx.SUB() != null) {
                expr = ctx.expression(0).accept(ExpressionVisitor.INSTANCE);
                if (expr instanceof Literal) {
                    expr = new Literal(((Literal) expr).getValue().not()); // negate numbers at compile time
                } else {
                    expr = new BinOp(new Literal(new TInt(0)), Operator.SUB, expr);
                }
            } else if (ctx.NOT() != null) {
                expr = new BinOp(null, Operator.NOT, expr);
            }
        } else if (ctx.expression().size() == 2) {
            Expression left = ctx.expression(0).accept(ExpressionVisitor.INSTANCE);
            Expression right = ctx.expression(1).accept(ExpressionVisitor.INSTANCE);
            Operator operator = getOperator(ctx);
            expr = new BinOp(left, operator, right);
        } else {
            Errors.put("Unsupported expression: " + ctx.getText());
        }

        if (expr != null) {
            expr.setLineNumber(ctx.start.getLine());
            expr.setText(ctx.getText());
        }

        return expr;
    }

    private Expression getSlice(final RavenParser.ExpressionContext ctx) {
        QualifiedName funName = new QualifiedName("subList");

        Expression lst = ctx.lst.accept(this);
        Expression start = new Literal(new TInt(0));

        Expression end = new Call(lst, new QualifiedName("size"));
        if (ctx.lhs != null) {
            start = ctx.lhs.accept(ExpressionVisitor.INSTANCE);
        }
        if (ctx.rhs != null) {
            end = ctx.rhs.accept(ExpressionVisitor.INSTANCE);
        }
        return new Call(lst, funName, start, end);
    }

    private static Operator getOperator(final RavenParser.ExpressionContext ctx) {
        if (ctx.EXP() != null) {
            return Operator.EXP;
        } else if (ctx.MULT() != null) {
            return Operator.MULT;
        } else if (ctx.DIV() != null) {
            return Operator.DIV;
        } else if (ctx.MOD() != null) {
            return Operator.MOD;
        } else if (ctx.ADD() != null) {
            return Operator.ADD;
        } else if (ctx.SUB() != null) {
            return Operator.SUB;
        } else if (ctx.AND() != null) {
            return Operator.AND;
        } else if (ctx.OR() != null) {
            return Operator.OR;
        } else if (ctx.GT() != null) {
            return Operator.GT;
        } else if (ctx.LT() != null) {
            return Operator.LT;
        } else if (ctx.GTE() != null) {
            return Operator.GTE;
        } else if (ctx.LTE() != null) {
            return Operator.LTE;
        } else if (ctx.EQUALS() != null) {
            return Operator.EQ;
        } else if (ctx.NOT_EQUAL() != null) {
            return Operator.NE;
        }
        return null;
    }

    public static final ExpressionVisitor INSTANCE = new ExpressionVisitor();
}
