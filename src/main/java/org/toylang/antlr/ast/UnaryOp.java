package org.toylang.antlr.ast;

public class UnaryOp extends Expression {

    private final int op;
    private final Expression expr;

    public UnaryOp(int op, Expression expr) {
        this.op = op;
        this.expr = expr;
    }

    public int getOp() {
        return op;
    }

    public Expression getExpr() {
        return expr;
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.visitUnaryOp(this);
    }

    @Override
    public String toString() {
        return op + expr.toString();
    }
}
