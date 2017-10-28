package org.toylang.antlr.ast;

public class Break extends Statement {

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.visitBreak();
    }
}
