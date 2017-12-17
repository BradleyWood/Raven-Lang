package org.toylang.antlr.ast;

import java.util.Arrays;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ListDef listDef = (ListDef) o;
        return Arrays.equals(expressions, listDef.expressions);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(expressions);
        return result;
    }
}
