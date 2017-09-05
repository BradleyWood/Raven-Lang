package org.toylang.antlr.ast;

public class Return extends Statement {

    private final Expression value;

    public Return(Expression value) {
        this.value = value;
    }
    public Expression getValue() {
        return value;
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.visitReturn(this);
    }

    @Override
    public String toString() {
        return "return" + (value != null ? " " + value.toString() : "") + ";";
    }
}
