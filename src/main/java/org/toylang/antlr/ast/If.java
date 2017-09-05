package org.toylang.antlr.ast;

public class If extends Statement {

    private final Expression condition;
    private final Statement body;
    private final Statement else_;

    public If(Expression condition, Statement body, Statement else_) {
        this.condition = condition;
        this.body = body;
        this.else_ = else_;
    }
    public Expression getCondition() {
        return condition;
    }
    public Statement getBody() {
        return body;
    }
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
