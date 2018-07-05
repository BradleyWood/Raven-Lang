package org.raven.antlr.ast;

import java.util.Arrays;

/**
 * A fully qualified name can be used to represent any name,
 * including imported resources, variables, functions, annotations
 * classes, etc.
 */
public class QualifiedName extends Expression {

    private String[] names;

    /**
     * Initializes a fully qualified name with the specified names
     * @param names The names
     */
    public QualifiedName(final String... names) {
        this.names = names;
    }

    /**
     * Get all of the names in the fully qualified name
     * @return The names
     */
    public String[] getNames() {
        return names;
    }

    public boolean isEmpty() {
        return names.length == 0;
    }

    @Override
    public String toString() {
        if (names == null || names.length == 0)
            return null;
        StringBuilder qualifiedName = new StringBuilder();
        for (String name : names) {
            qualifiedName.append(name).append(".");
        }
        return qualifiedName.substring(0, qualifiedName.length() - 1);
    }

    /**
     * Adds an element to the fully qualified name. Does not modify this object
     *
     * @param name The name to add
     * @return The new qualified name
     */
    public QualifiedName add(final String name) {
        String[] sa = new String[names.length + 1];
        System.arraycopy(names, 0, sa, 0, names.length);
        sa[names.length] = name;
        return new QualifiedName(sa);
    }

    /**
     * Adds a fully qualified name to this object and returns the result.
     * Does not modify this object.
     * @param name The fqn to add
     * @return The new name
     */
    public QualifiedName add(final QualifiedName name) {
        String[] sa = new String[names.length + name.getNames().length];
        int i;
        for (i = 0; i < names.length; i++) {
            sa[i] = names[i];
        }
        for (String s : name.getNames()) {
            sa[i] = s;
            i++;
        }
        return new QualifiedName(sa);
    }

    @Override
    public void accept(final TreeVisitor visitor) {
        visitor.visitName(this);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QualifiedName that = (QualifiedName) o;
        return Arrays.equals(names, that.names);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(names);
    }

    public static QualifiedName valueOf(final String str) {
        return new QualifiedName(str.split("\\."));
    }
}