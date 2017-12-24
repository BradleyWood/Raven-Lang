package org.toylang.antlr.ast;

import java.util.Objects;

public class For extends Statement {

    private final Statement init;
    private final Expression condition;
    private final Statement body;
    private final Statement after;

    public For(Statement init, Expression condition, Statement body, Statement after) {
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
    public void accept(TreeVisitor visitor) {
        visitor.visitFor(this);
    }

    @Override
    public boolean equals(Object o) {
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
        return Objects.hash(super.hashCode(), init, condition, body, after);
    }
}
