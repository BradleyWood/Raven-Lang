package org.raven.antlr.ast;

import java.util.Objects;

public class Raise extends Statement {

    private final Expression expression;

    public Raise(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.visitRaise(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Raise raise = (Raise) o;
        return Objects.equals(expression, raise.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), expression);
    }
}
