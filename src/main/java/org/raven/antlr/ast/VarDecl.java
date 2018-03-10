package org.raven.antlr.ast;

import org.raven.antlr.Modifier;
import org.raven.core.wrappers.TNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class VarDecl extends Statement {


    private final QualifiedName name;
    private final Expression initialValue;
    private List<Modifier> modifiers;
    private boolean isJavaField = false;

    public VarDecl(QualifiedName name, Expression initialValue, Modifier... modifiers) {
        this(name, initialValue, new ArrayList<>(Arrays.asList(modifiers)));
    }

    public VarDecl(QualifiedName name, Expression initialValue, List<Modifier> modifiers) {
        this.name = name;
        this.initialValue = initialValue;
        this.modifiers = modifiers;
    }

    public void setJavaField(boolean isJavaField) {
        this.isJavaField = isJavaField;
    }

    public boolean isJavaField() {
        return isJavaField;
    }

    public QualifiedName getName() {
        return name;
    }

    public List<Modifier> getModifiers() {
        return modifiers;
    }

    public void addModifier(Modifier modifier) {
        modifiers.add(modifier);
    }

    public boolean hasModifier(Modifier modifier) {
        for (Modifier m : modifiers) {
            if (m.equals(modifier))
                return true;
        }
        return false;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VarDecl decl = (VarDecl) o;
        return Objects.equals(name, decl.name) &&
                Objects.equals(initialValue, decl.initialValue) &&
                Objects.equals(modifiers, decl.modifiers) &&
                Objects.equals(getAnnotations(), decl.getAnnotations());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, initialValue);
        result = 31 * result + Objects.hashCode(modifiers);
        return result;
    }

    @Override

    public String toString() {
        return "var " + name + " = " + initialValue;
    }
}
