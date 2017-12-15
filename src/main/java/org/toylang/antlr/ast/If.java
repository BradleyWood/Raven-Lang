package org.toylang.antlr.ast;

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
    public If(Expression condition, Statement body, Statement else_) {
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
    public void accept(TreeVisitor visitor) {
        visitor.visitIf(this);
    }

    @Override
    public String toString() {
        return "if(" + condition.toString() + ")" + body.toString() +
                (else_ != null ? (" else " + else_.toString()) : "");
    }
}
