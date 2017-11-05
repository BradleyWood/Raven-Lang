package org.toylang.antlr.ast;

public class Inheritance {

    private final QualifiedName super_;
    private final Expression[] superParams;

    private final QualifiedName[] interfaces;

    public Inheritance(QualifiedName superClass, Expression[] superParams, QualifiedName[] interfaces) {
        this.super_ = superClass;
        this.superParams = superParams;
        this.interfaces = interfaces;
    }

    public QualifiedName getSuperClass() {
        return super_;
    }

    public Expression[] getSuperParams() {
        return superParams;
    }

    public QualifiedName[] getInterfaces() {
        return interfaces;
    }
}
