package org.raven.antlr.ast;

import java.util.Arrays;
import java.util.Objects;

public class AnnoDef extends Statement {

    private final QualifiedName name;
    private final QualifiedName[] paramNames;

    /**
     * Initializes an annotation definition
     *
     * @param name The name of the annotation
     * @param paramNames The names of annotation parameters
     */
    public AnnoDef(final QualifiedName name, final QualifiedName... paramNames) {
        this.name = name;
        this.paramNames = paramNames;
    }

    /**
     * Get the name of this annotation
     * @return The qualified name
     */
    public QualifiedName getName() {
        return name;
    }

    /**
     * Get the list of annotation parameters names
     * @return The list of parameters
     */
    public QualifiedName[] getParamNames() {
        return paramNames;
    }

    @Override
    public void accept(final TreeVisitor visitor) {
        visitor.visitAnnotationDef(this);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnnoDef annoDef = (AnnoDef) o;
        return Objects.equals(name, annoDef.name) &&
                Arrays.equals(paramNames, annoDef.paramNames);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name);
        result = 31 * result + Arrays.hashCode(paramNames);
        return result;
    }
}
