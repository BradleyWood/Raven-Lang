package org.toylang.antlr.ast;

public class While extends Statement {

    private final Expression condition;
    private final Statement body;

    public While(Expression condition, Statement body) {
        this.condition = condition;
        this.body = body;
    }
    public Expression getCondition() {
        return condition;
    }
    public Statement getBody() {
        return body;
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
