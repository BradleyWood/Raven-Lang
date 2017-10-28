package org.toylang.antlr.ast;

public class Continue extends Statement {
    @Override
    public void accept(TreeVisitor visitor) {
        visitor.visitContinue();
    }
}
