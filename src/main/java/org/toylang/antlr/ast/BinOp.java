package org.toylang.antlr.ast;

import org.toylang.antlr.Operator;

public class BinOp extends Expression {

    private final Expression left;
    private final Operator op;
    private final Expression right;

    public BinOp(Expression left, Operator op, Expression right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }
    public Expression getLeft() {
        return left;
    }
    public Operator getOp() {
        return op;
    }
    public Expression getRight() {
        return right;
    }
    @Override
    public void accept(TreeVisitor visitor) {
        visitor.visitBinOp(this);
    }
    @Override
    public String toString() {
        return left.toString() + " " + op + " " + right.toString();
    }
}
