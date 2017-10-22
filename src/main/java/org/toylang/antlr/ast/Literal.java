package org.toylang.antlr.ast;

import org.toylang.core.wrappers.TObject;

public class Literal extends Expression {

    private final TObject value;

    public Literal(TObject value) {
        this.value = value;
    }
    public TObject getValue() {
        return value;
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.visitLiteral(this);
    }
}
