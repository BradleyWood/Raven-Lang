package org.toylang.antlr.ast;

public class ListDef extends Expression {

    private final Expression[] expressions;

    /**
     * Initializes a list holding specified expressions
     * @param expressions The list of expressions in the list
     */
    public ListDef(Expression... expressions) {
        this.expressions = expressions;
    }

    /**
     * Get the list of expression in this list
     * @return
     */
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
