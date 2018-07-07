package org.raven.antlr.ast;

import java.util.Objects;

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final TernaryOp ternaryOp = (TernaryOp) o;
        return Objects.equals(condition, ternaryOp.condition) &&
                Objects.equals(lhs, ternaryOp.lhs) &&
                Objects.equals(rhs, ternaryOp.rhs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), condition, lhs, rhs);
    }
}
