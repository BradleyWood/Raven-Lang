package org.toylang.antlr.ast;

import java.util.Objects;

public class Return extends Statement {

    private final Expression value;

    public Return(Expression value) {
        this.value = value;
    }

    public Expression getValue() {
        return value;
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.visitReturn(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Return aReturn = (Return) o;
        return Objects.equals(value, aReturn.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value);
    }

    @Override
    public String toString() {
        return "return" + (value != null ? " " + value.toString() : "") + ";";
    }
}
