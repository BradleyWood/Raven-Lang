package org.toylang.antlr.visitor;

import org.toylang.antlr.Operator;
import org.toylang.antlr.ToyLangBaseVisitor;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.ast.*;
import org.toylang.core.wrappers.*;

public class ExpressionVisitor extends ToyLangBaseVisitor<Expression> {

    private ExpressionVisitor() {
    }

    @Override
    public Expression visitExpression(ToyLangParser.ExpressionContext ctx) {
        Expression expr = null;

        if (ctx.expression().size() == 1 && ctx.qualifiedName() != null) {
            QualifiedName qn = ctx.qualifiedName().accept(QualifiedNameVisitor.INSTANCE);
            Expression preceeding = ctx.expression(0).accept(ExpressionVisitor.INSTANCE);
            for (String s : qn.getNames()) {
                preceeding = new Call(preceeding, new QualifiedName("getField"), new QualifiedName(s));
            }
            expr = preceeding;
        } else if (ctx.expression().size() == 1 && ctx.funCall() != null) {
            QualifiedName name = new QualifiedName(ctx.funCall().IDENTIFIER().getText());
            Expression preceedingExpr = ctx.expression(0).accept(ExpressionVisitor.INSTANCE);
            preceedingExpr.setLineNumber(ctx.expression(0).start.getLine());
            Expression[] expressions = new Expression[0];
            if (ctx.funCall().paramList() != null)
                expressions = new Expression[ctx.funCall().paramList().param().size()];
            for (int i = 0; i < expressions.length; i++) {
                expressions[i] = ctx.funCall().paramList().param(i).accept(this);
            }
            expr = new Call(preceedingExpr, name, expressions);
            expr.setLineNumber(ctx.funCall().start.getLine());
        } else if (ctx.literal() != null) {
            if (ctx.literal().number() != null) {
                if (ctx.literal().number().getText().contains(".") || ctx.literal().number().getText().toLowerCase().contains("e")) {
                    expr = new Literal(new TReal(Double.parseDouble(ctx.literal().number().getText())));
                } else {
                    expr = new Literal(new TInt(Integer.parseInt(ctx.literal().number().getText())));
                }
            } else if (ctx.literal().stringLiteral() != null) {
                String str = ctx.literal().stringLiteral().getText();
                expr = new Literal(new TString(str.substring(1, str.length() - 1)));
            } else if (ctx.literal().getText().equals("null")) {
                expr = new Literal(TNull.NULL); // use reference to null instead
            } else if (ctx.literal().getText().equals("true")) {
                expr = new Literal(TBoolean.TRUE); // use reference
            } else if (ctx.literal().getText().equals("false")) {
                expr = new Literal(TBoolean.FALSE); // use reference
            }
            assert expr != null;
            expr.setLineNumber(ctx.literal().start.getLine());
        } else if (ctx.dict() != null) {
            expr = ctx.dict().accept(DictDefVisitor.INSTANCE);
            expr.setLineNumber(ctx.dict().start.getLine());
        } else if (ctx.list() != null) {
            expr = ctx.list().accept(ArrayDefVisitor.INSTANCE);
            expr.setLineNumber(ctx.list().start.getLine());
        } else if (ctx.listIdx() != null) {
            expr = ctx.listIdx().accept(ListIndexVisitor.INSTANCE);
            expr.setLineNumber(ctx.listIdx().start.getLine());
        } else if (ctx.funCall() != null) {
            // todo;
            QualifiedName name = new QualifiedName(ctx.funCall().IDENTIFIER().getText());
            Expression[] expressions = new Expression[0];
            if (ctx.funCall().paramList() != null)
                expressions = new Expression[ctx.funCall().paramList().param().size()];
            for (int i = 0; i < expressions.length; i++) {
                expressions[i] = ctx.funCall().paramList().param(i).accept(this);
            }
            expr = new Call(name, expressions);
            expr.setLineNumber(ctx.funCall().start.getLine());
        } else if (ctx.qualifiedName() != null) {
            expr = ctx.qualifiedName().accept(QualifiedNameVisitor.INSTANCE);
            expr.setLineNumber(ctx.qualifiedName().start.getLine());
        } else if (ctx.varAssignment() != null) {
            expr = ctx.varAssignment().accept(AssignmentVisitor.INSTANCE);
            expr.setLineNumber(ctx.varAssignment().start.getLine());
        } else if (ctx.expression().size() == 1) {
            expr = ctx.expression(0).accept(ExpressionVisitor.INSTANCE);
            if (ctx.ADD() != null) {

            } else if (ctx.SUB() != null) {
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
            //System.out.println(left + " : "+operator + " : "+right);
            expr = new BinOp(left, operator, right);
        } else {
            // error
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
