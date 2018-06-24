package org.raven.antlr.visitor;

import org.raven.error.Errors;
import org.raven.antlr.Operator;
import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ast.BinOp;
import org.raven.antlr.ast.Expression;

public class AssignmentVisitor extends RavenBaseVisitor<BinOp> {

    private AssignmentVisitor() {
    }

    @Override
    public BinOp visitVarAssignment(final RavenParser.VarAssignmentContext ctx) {
        Expression lhs = null;
        if (ctx.qualifiedName() != null) {
            lhs = ctx.qualifiedName().accept(QualifiedNameVisitor.INSTANCE);
        }
        Operator operator = getOperator(ctx);
        Expression value = ctx.accept(ExpressionVisitor.INSTANCE);

        if (operator != Operator.ASSIGNMENT) {
            value = new BinOp(lhs, operator, value);
        }

        BinOp bop = new BinOp(lhs, Operator.ASSIGNMENT, value);

        if (lhs != null)
            lhs.setParent(bop);

        value.setParent(bop);

        return bop;
    }

    private Operator getOperator(final RavenParser.VarAssignmentContext ctx) {
        if (ctx.ASSIGNMENT() != null) {
            return Operator.ASSIGNMENT;
        } else if (ctx.ADD_ASSIGNMENT() != null) {
            return Operator.ADD;
        } else if (ctx.SUB_ASSIGNMENT() != null) {
            return Operator.SUB;
        } else if (ctx.MULT_ASSIGNMENT() != null) {
            return Operator.MULT;
        } else if (ctx.DIV_ASSIGNMENT() != null) {
            return Operator.DIV;
        } else if (ctx.MOD_ASSIGNMENT() != null) {
            return Operator.MOD;
        } else if (ctx.EXP_ASSIGNMENT() != null) {
            return Operator.EXP;
        } else {
            Errors.put("at line " + ctx.start.getLine() + ":" + ctx.start.getCharPositionInLine() + " illegal assignment");
        }
        return null;
    }

    public static final AssignmentVisitor INSTANCE = new AssignmentVisitor();
}
