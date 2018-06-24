package org.raven.antlr.ast;

import java.util.Objects;

public class If extends Statement {

    private final Expression condition;
    private final Statement body;
    private final Statement else_;

    /**
     * Initializes an if statement
     * @param condition The statement
     * @param body
     * @param else_
     */
    public If(final Expression condition, final Statement body, final Statement else_) {
        this.condition = condition;
        this.body = body;
        this.else_ = else_;
    }

    /**
     * Get the conditional expression for this if statement
     * @return The expression
     */
    public Expression getCondition() {
        return condition;
    }

    /**
     * Get the body to be executed if the conditional is true
     * @return The statement or block
     */
    public Statement getBody() {
        return body;
    }

    /**
     * Get the body to be executed if the conditional is false
     * @return The statement or block
     */
    public Statement getElse() {
        return else_;
    }

    @Override
    public void accept(final TreeVisitor visitor) {
        visitor.visitIf(this);
    }

    @Override
    public String toString() {
        return "if(" + condition.toString() + ")" + body.toString() +
                (else_ != null ? (" else " + else_.toString()) : "");
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        If anIf = (If) o;
        return Objects.equals(condition, anIf.condition) &&
                Objects.equals(body, anIf.body) &&
                Objects.equals(else_, anIf.else_);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition, body, else_);
    }
}
