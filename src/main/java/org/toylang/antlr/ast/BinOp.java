package org.toylang.antlr.ast;

import org.toylang.antlr.Operator;

import java.util.Objects;

public class BinOp extends Expression {

    private final Expression left;
    private final Operator op;
    private final Expression right;

    /**
     * Initializes a binary operation
     *
     * @param left The left-hand-side of the expression
     * @param op The operator to apply
     * @param right The right-hand-side of the expression
     */
    public BinOp(Expression left, Operator op, Expression right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }

    /**
     * Get the left-hand-side of the binary op expression
     * @return The expression
     */
    public Expression getLeft() {
        return left;
    }

    /**
     * Get the operator used in this expression
     * @return The Operator
     */
    public Operator getOp() {
        return op;
    }

    /**
     * Get the right-hand-sidde of the binary op expression
     * @return
     */
    public Expression getRight() {
        return right;
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.visitBinOp(this);
    }

    @Override
    public String toString() {
        return left + " " + op + " " + right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BinOp binOp = (BinOp) o;
        return Objects.equals(left, binOp.left) &&
                op == binOp.op &&
                Objects.equals(right, binOp.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), left, op, right);
    }
}
