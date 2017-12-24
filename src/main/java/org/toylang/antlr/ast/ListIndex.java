package org.toylang.antlr.ast;

import java.util.Arrays;
import java.util.Objects;

/**
 * References an index in an array or dictionary
 */
public class ListIndex extends Expression {

    private final QualifiedName name;
    private final Expression[] index;

    /**
     * Initializes a list index expression
     * @param name
     * @param index
     */
    public ListIndex(QualifiedName name, Expression... index) {
        this.name = name;
        this.index = index;
    }

    /**
     * Get the name of the list to index
     * @return The fully qualified list name
     */
    public QualifiedName getName() {
        return name;
    }

    /**
     * Get the element indicies
     * @return The indicies as a list of expression
     */
    public Expression[] getIndex() {
        return index;
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.visitListIdx(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListIndex listIndex = (ListIndex) o;
        return Objects.equals(name, listIndex.name) &&
                Arrays.equals(index, listIndex.index);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), name);
        result = 31 * result + Arrays.hashCode(index);
        return result;
    }
}
