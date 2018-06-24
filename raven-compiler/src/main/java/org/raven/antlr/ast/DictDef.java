package org.raven.antlr.ast;


import java.util.Arrays;

public class DictDef extends Expression {

    private final Expression[] keys;
    private final Expression[] values;

    public DictDef(final Expression[] keys, final Expression[] values) {
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
    public void accept(final TreeVisitor visitor) {
        visitor.visitDictDef(this);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DictDef dictDef = (DictDef) o;
        return Arrays.equals(keys, dictDef.keys) &&
                Arrays.equals(values, dictDef.values);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(keys);
        result = 31 * result + Arrays.hashCode(values);
        return result;
    }
}
