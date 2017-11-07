package org.toylang.antlr.ast;

public class Annotation extends Statement {

    private final String name;
    private final Expression[] params;

    public Annotation(String name, Expression... params) {
        this.name = name;
        this.params = params;
    }

    public String getName() {
        return name;
    }

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
