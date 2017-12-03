package org.toylang.antlr.ast;

import org.toylang.antlr.Modifier;
import org.toylang.core.wrappers.TNull;

public class VarDecl extends Statement {


    private final QualifiedName name;
    private final Expression initialValue;
    private Modifier[] modifiers;

    public VarDecl(QualifiedName name, Expression initialValue, Modifier... modifiers) {
        this.name = name;
        this.initialValue = initialValue;
        this.modifiers = modifiers;
    }

    public QualifiedName getName() {
        return name;
    }

    public Modifier[] getModifiers() {
        return modifiers;
    }

    public void setModifiers(Modifier... modifiers) {
        this.modifiers = modifiers;
    }

    public int modifiers() {
        int mod = 0;
        for (Modifier modifier : getModifiers()) {
            mod += modifier.getModifier();
        }
        return mod;
    }

    public Expression getInitialValue() {
        return initialValue != null ? initialValue : new Literal(TNull.NULL);
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.visitVarDecl(this);
    }

    @Override
    public String toString() {
        return "var " + name + " = " + initialValue;
    }
}
