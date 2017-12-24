package org.toylang.antlr.ast;


import java.util.Arrays;

public class DictDef extends Expression {

    private final Expression[] keys;
    private final Expression[] values;

    public DictDef(Expression[] keys, Expression[] values) {
        this.keys = keys;
        this.values = values;
    }

    public Expression[] getKeys() {
        return keys;
    }

    public Expression[] getValues() {
        return values;
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.visitDictDef(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DictDef dictDef = (DictDef) o;
        return Arrays.equals(keys, dictDef.keys) &&
                Arrays.equals(values, dictDef.values);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(keys);
        result = 31 * result + Arrays.hashCode(values);
        return result;
    }
}
