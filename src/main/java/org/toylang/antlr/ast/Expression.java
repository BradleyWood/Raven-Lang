package org.toylang.antlr.ast;

import java.util.Objects;

public class Expression extends Statement {

    private boolean pop = false;

    @Override
    public void accept(TreeVisitor visitor) {
        throw new RuntimeException("Invalid Expression Node");
    }

    /**
     * Sometimes the return value is not used and it must be popped off the stack
     *
     * @return True if the return value should be popped off the stack
     */
    public boolean pop() {
        return pop;
    }

    /**
     * If the result of this expression is not used, we may need to pop the value off the stack
     *
     * @param pop Whether to pop the return value off the stack
     */
    public void setPop(boolean pop) {
        this.pop = pop;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Expression that = (Expression) o;
        return lineNumber == that.lineNumber && pop == that.pop;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), pop);
    }
}
