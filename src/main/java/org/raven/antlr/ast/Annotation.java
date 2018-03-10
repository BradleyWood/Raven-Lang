package org.raven.antlr.ast;

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
        if (keys.length != values.length)
            throw new IllegalArgumentException("Requires equal number of keys and values");
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

    public Literal get(QualifiedName name) {
        for (int i = 0; i < keys.length; i++) {
            if (keys[i].equals(name)) {
                return values[i];
            }
        }
        return null;
    }

    public Literal get(String name) {
        return get(new QualifiedName(name));
    }

    public Literal getOrDefault(QualifiedName name, Literal defaultValue) {
        Literal val = get(name);
        if (val != null)
            return val;
        return defaultValue;
    }

    public Literal getOrDefault(String name, Literal defaultValue) {
        return getOrDefault(new QualifiedName(name), defaultValue);
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
        int result = Objects.hash(name);
        result = 31 * result + Arrays.hashCode(keys);
        result = 31 * result + Arrays.hashCode(values);
        return result;
    }
}
