package org.raven.antlr.ast;

import java.util.Objects;

public class For extends Statement {

    private final Statement init;
    private final Expression condition;
    private final Statement body;
    private final Statement after;

    public For(final Statement init, final Expression condition, final Statement body, final Statement after) {
        this.init = init;
        this.condition = condition;
        this.body = body;
        this.after = after;
    }

    public Statement getInit() {
        return init;
    }

    public Expression getCondition() {
        return condition;
    }

    public Statement getBody() {
        return body;
    }

    public Statement getAfter() {
        return after;
    }

    @Override
    public void accept(final TreeVisitor visitor) {
        visitor.visitFor(this);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        For aFor = (For) o;
        return Objects.equals(init, aFor.init) &&
                Objects.equals(condition, aFor.condition) &&
                Objects.equals(body, aFor.body) &&
                Objects.equals(after, aFor.after);
    }

    @Override
    public int hashCode() {
        return Objects.hash(init, condition, body, after);
    }
}
