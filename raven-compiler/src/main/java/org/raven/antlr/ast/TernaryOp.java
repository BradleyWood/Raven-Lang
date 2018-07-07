package org.raven.antlr.ast;

public class TernaryOp extends Expression {

    private final Expression condition;
    private final Expression lhs;
    private final Expression rhs;

    public TernaryOp(final Expression condition, final Expression lhs, final Expression rhs) {
        this.condition = condition;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public Expression getCondition() {
        return condition;
    }

    public Expression getLhs() {
        return lhs;
    }

    public Expression getRhs() {
        return rhs;
    }

    @Override
    public void accept(final TreeVisitor visitor) {
        visitor.visitTernaryOp(this);
    }
}
