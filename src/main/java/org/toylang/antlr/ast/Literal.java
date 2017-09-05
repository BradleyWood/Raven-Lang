package org.toylang.antlr.ast;

import org.toylang.core.ToyObject;

public class Literal extends Expression {

    private final ToyObject value;

    public Literal(ToyObject value) {
        this.value = value;
    }
    public ToyObject getValue() {
        return value;
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.visitLiteral(this);
    }
}
