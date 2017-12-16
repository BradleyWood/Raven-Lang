package org.toylang.antlr.ast;

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
}
