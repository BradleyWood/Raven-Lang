package org.raven.antlr.ast;

import java.util.Objects;

public class While extends Statement {

    private final Expression condition;
    private final Statement body;
    private final boolean doWhile;

    public While(final Expression condition, final Statement body, final boolean doWhile) {
        this.condition = condition;
        this.body = body;
        this.doWhile = doWhile;
    }

    public While(final Expression condition, final Statement body) {
        this(condition, body, false);
    }

    public Expression getCondition() {
        return condition;
    }

    public Statement getBody() {
        return body;
    }

    public boolean isDoWhile() {
        return doWhile;
    }

    @Override
    public void accept(final TreeVisitor visitor) {
        visitor.visitWhile(this);
    }

    @Override
    public String toString() {
        return "while(" + condition.toString() + ") " + body.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        While aWhile = (While) o;
        return doWhile == aWhile.doWhile &&
                Objects.equals(condition, aWhile.condition) &&
                Objects.equals(body, aWhile.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition, body, doWhile);
    }
}
