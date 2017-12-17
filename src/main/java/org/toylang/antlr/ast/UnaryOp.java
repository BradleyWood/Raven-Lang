package org.toylang.antlr.ast;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnaryOp unaryOp = (UnaryOp) o;
        return op == unaryOp.op &&
                Objects.equals(expr, unaryOp.expr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(op, expr);
    }

    @Override
    public String toString() {
        return op + expr.toString();
    }
}
