package org.toylang.antlr.ast;

public class Expression extends Statement {

    private int lineNumber = -1;

    @Override
    public void accept(TreeVisitor visitor) {
        throw new RuntimeException("Invalid Expression Node");
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
}
