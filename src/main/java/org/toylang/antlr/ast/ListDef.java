package org.toylang.antlr.ast;

public class ListDef extends Expression {

    private final Expression[] expressions;

    public ListDef(Expression... expressions) {
        this.expressions = expressions;
    }
    public Expression[] getExpressions() {
        return expressions;
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.visitListDef(this);
    }
    @Override
    public String toString() {
        String str = "[ ";
        for (Expression expression : expressions) {
            str += expression.toString() + " ";
        }
        return str + "]";
    }
}
