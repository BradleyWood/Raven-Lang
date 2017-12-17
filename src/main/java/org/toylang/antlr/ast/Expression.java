package org.toylang.antlr.ast;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Expression that = (Expression) o;
        return lineNumber == that.lineNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lineNumber);
    }
}
