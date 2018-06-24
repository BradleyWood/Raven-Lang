package org.raven.antlr.ast;

import java.util.Arrays;
import java.util.Objects;

/**
 * References an index in an array or dictionary
 */
public class ListIndex extends Expression {

    private Expression precedingExpr;
    private final Expression[] index;

    /**
     * Initializes a list index expression
     * @param precedingExpr expression that puts a list on the stack
     * @param index The indices
     */
    public ListIndex(final Expression precedingExpr, final Expression... index) {
        this.precedingExpr = precedingExpr;
        this.index = index;
    }

    /**
     * Get the name of the list to index
     * @return The fully qualified list name
     */
    public Expression getPrecedingExpr() {
        return precedingExpr;
    }

    public void setPrecedingExpr(final Expression precedingExpr) {
        this.precedingExpr = precedingExpr;
    }

    /**
     * Get the element indicies
     * @return The indicies as a list of expression
     */
    public Expression[] getIndex() {
        return index;
    }

    @Override
    public void accept(final TreeVisitor visitor) {
        visitor.visitListIdx(this);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListIndex listIndex = (ListIndex) o;
        return Objects.equals(precedingExpr, listIndex.precedingExpr) &&
                Arrays.equals(index, listIndex.index);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(precedingExpr);
        result = 31 * result + Arrays.hashCode(index);
        return result;
    }
}
