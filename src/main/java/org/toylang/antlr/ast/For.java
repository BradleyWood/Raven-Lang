package org.toylang.antlr.ast;

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
}
