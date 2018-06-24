package org.raven.antlr.ast;

import java.util.Arrays;

public class ListDef extends Expression {

    private final Expression[] expressions;

    /**
     * Initializes a list holding specified expressions
     * @param expressions The list of expressions in the list
     */
    public ListDef(final Expression... expressions) {
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
    public void accept(final TreeVisitor visitor) {
        visitor.visitListDef(this);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("[ ");
        for (Expression expression : expressions) {
            str.append(expression.toString()).append(" ");
        }
        return str + "]";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListDef listDef = (ListDef) o;
        return Arrays.equals(expressions, listDef.expressions);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(expressions);
    }
}
