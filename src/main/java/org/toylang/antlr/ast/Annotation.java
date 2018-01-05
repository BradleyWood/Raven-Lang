package org.toylang.antlr.ast;

import java.util.Arrays;
import java.util.Objects;

public class Annotation extends Statement {

    private final String name;
    private final QualifiedName[] keys;
    private final Literal[] values;

    /**
     * @param name   The name of the annotation type
     * @param keys The parameter names for the annotation
     * @param values The values associated with the param
     */
    public Annotation(String name, QualifiedName[] keys, Literal[] values) {
        this.name = name;
        this.keys = keys;
        this.values = values;
    }

    /**
     * @return The name of the annotation type
     */
    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return "@" + name;
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.visitAnnotation(this);
    }

    public QualifiedName[] getKeys() {
        return keys;
    }

    public Literal[] getValues() {
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Annotation that = (Annotation) o;
        return Objects.equals(name, that.name) &&
                Arrays.equals(keys, that.keys) &&
                Arrays.equals(values, that.values);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(super.hashCode(), name);
        result = 31 * result + Arrays.hashCode(keys);
        result = 31 * result + Arrays.hashCode(values);
        return result;
    }
}
