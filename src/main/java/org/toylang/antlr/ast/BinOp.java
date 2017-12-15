package org.toylang.antlr.ast;

import org.toylang.antlr.Operator;

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
}
