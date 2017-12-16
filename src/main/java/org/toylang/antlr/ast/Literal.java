package org.toylang.antlr.ast;

import org.toylang.core.wrappers.TObject;

/**
 * Holds a literal value such as a string, number, boolean value, null etc
 */
public class Literal extends Expression {

    private final TObject value;

    /**
     * Initializes a literal with a value
     * @param value The literal value
     */
    public Literal(TObject value) {
        this.value = value;
    }

    /**
     * Get the literal value
     * @return The value as a wrapped object
     */
    public TObject getValue() {
        return value;
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.visitLiteral(this);
    }
}
