package org.toylang.antlr.ast;

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
}
