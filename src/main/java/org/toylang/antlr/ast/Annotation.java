package org.toylang.antlr.ast;

import java.util.Arrays;
import java.util.Objects;

public class Annotation extends Statement {

    private final String name;
    private final Expression[] params;

    /**
     * @param name The name of the annotation type
     * @param params The parameters for the annotation
     */
    public Annotation(String name, Expression... params) {
        this.name = name;
        this.params = params;
    }

    /**
     *
     * @return The name of the annotation type
     */
    public String getName() {
        return name;
    }

    /**
     * Get the annotation parameters
     *
     * @return Annotation parameters in expression form
     */
    public Expression[] getParams() {
        return params;
    }

    @Override
    public String toString() {
        return "@" + name;
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.visitAnnotation(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Annotation that = (Annotation) o;
        return Objects.equals(name, that.name) &&
                Arrays.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), name);
        result = 31 * result + Arrays.hashCode(params);
        return result;
    }
}
