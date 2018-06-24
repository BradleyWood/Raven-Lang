package org.raven.antlr.ast;

import org.raven.antlr.Modifier;
import org.raven.core.wrappers.TNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class VarDecl extends ModifiableStatement {

    private static final String TYPE = "Lorg/raven/core/wrappers/TObject;";

    private final QualifiedName name;
    private final Expression initialValue;
    private String typeDesc = TYPE;

    public VarDecl(final QualifiedName name, final Expression initialValue, final Modifier... modifiers) {
        this(name, initialValue, new ArrayList<>(Arrays.asList(modifiers)));
    }

    public VarDecl(final QualifiedName name, final Expression initialValue, final List<Modifier> modifiers) {
        super(modifiers);
        this.name = name;
        this.initialValue = initialValue;
    }

    public void setType(final String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public boolean isJavaField() {
        return !typeDesc.equals(TYPE);
    }

    public QualifiedName getName() {
        return name;
    }

    public Expression getInitialValue() {
        return initialValue != null ? initialValue : new Literal(TNull.NULL);
    }

    @Override
    public void accept(final TreeVisitor visitor) {
        visitor.visitVarDecl(this);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VarDecl decl = (VarDecl) o;
        return Objects.equals(name, decl.name) &&
                Objects.equals(initialValue, decl.initialValue) &&
                Objects.equals(getModifiers(), decl.getModifiers()) &&
                Objects.equals(getAnnotations(), decl.getAnnotations());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, initialValue);
        result = 31 * result + Objects.hashCode(getModifiers());
        return result;
    }

    @Override

    public String toString() {
        return "var " + name + " = " + initialValue;
    }
}
