package org.toylang.antlr.ast;

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
    public void accept(TreeVisitor visitor) {
        visitor.visitAnnotationDef(this);
    }
}
