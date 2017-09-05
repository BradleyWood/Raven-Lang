package org.toylang.antlr.visitor;

import org.toylang.antlr.Operator;
import org.toylang.antlr.ToyLangBaseVisitor;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.ast.BinOp;
import org.toylang.antlr.ast.Expression;
import org.toylang.antlr.ast.QualifiedName;

public class AssignmentVisitor extends ToyLangBaseVisitor<BinOp> {

    private AssignmentVisitor() {}

    @Override
    public BinOp visitVarAssignment(ToyLangParser.VarAssignmentContext ctx) {
        QualifiedName name = ctx.qualifiedName().accept(QualifiedNameVisitor.INSTANCE);
        Operator operator = getOperator(ctx);
        Expression value = ctx.accept(ExpressionVisitor.INSTANCE);

        if(operator != Operator.ASSIGNMENT) {
            value = new BinOp(name, operator, value);
        }

        return new BinOp(name, Operator.ASSIGNMENT, value);
    }
    private Operator getOperator(ToyLangParser.VarAssignmentContext ctx) {
        if(ctx.ASSIGNMENT() != null) {
            return Operator.ASSIGNMENT;
        } else if(ctx.ADD_ASSIGNMENT() != null) {
            return Operator.ADD;
        } else if(ctx.SUB_ASSIGNMENT() != null) {
            return Operator.SUB;
        } else if(ctx.MULT_ASSIGNMENT() != null) {
            return Operator.MULT;
        } else if(ctx.DIV_ASSIGNMENT() != null) {
            return Operator.DIV;
        } else if(ctx.MOD_ASSIGNMENT() != null) {
            return Operator.MOD;
        } else if(ctx.EXP_ASSIGNMENT() != null) {
            return Operator.EXP;
        } else {
            System.err.println("Unknown assignment operator: "+ctx.getText());
        }
        return null;
    }

    public static final AssignmentVisitor INSTANCE = new AssignmentVisitor();
}
