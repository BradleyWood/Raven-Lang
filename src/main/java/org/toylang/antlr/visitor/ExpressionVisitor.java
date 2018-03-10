package org.toylang.antlr.visitor;

import org.toylang.antlr.Operator;
import org.toylang.antlr.ToyLangBaseVisitor;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.ast.*;
import org.toylang.error.Errors;
import org.toylang.core.wrappers.*;

public class ExpressionVisitor extends ToyLangBaseVisitor<Expression> {

    private ExpressionVisitor() {
    }

    @Override
    public Expression visitExpression(ToyLangParser.ExpressionContext ctx) {
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
        } else if (ctx.dict() != null) {
            expr = ctx.dict().accept(DictDefVisitor.INSTANCE);
        } else if (ctx.list() != null) {
            expr = ctx.list().accept(ArrayDefVisitor.INSTANCE);
        } else if (ctx.slice() != null) {
            expr = ctx.slice().accept(SliceVisitor.INSTANCE);
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

    private static Operator getOperator(ToyLangParser.ExpressionContext ctx) {
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
