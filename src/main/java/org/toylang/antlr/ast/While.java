package org.toylang.antlr.ast;

public class While extends Statement {

    private final Expression condition;
    private final Statement body;
    private final boolean doWhile;

    public While(Expression condition, Statement body, boolean doWhile) {
        this.condition = condition;
        this.body = body;
        this.doWhile = doWhile;
    }

    public While(Expression condition, Statement body) {
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
    public void accept(TreeVisitor visitor) {
        visitor.visitWhile(this);
    }

    @Override
    public String toString() {
        return "while(" + condition.toString() + ") " + body.toString();
    }
}
