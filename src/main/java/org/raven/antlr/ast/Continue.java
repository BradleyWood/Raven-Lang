package org.raven.antlr.ast;

public class Continue extends Statement {
    @Override
    public void accept(final TreeVisitor visitor) {
        visitor.visitContinue();
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
