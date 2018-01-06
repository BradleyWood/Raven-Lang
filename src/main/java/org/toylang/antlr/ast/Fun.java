package org.toylang.antlr.ast;

import org.toylang.antlr.Modifier;
import org.toylang.compiler.Constants;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class Fun extends Statement {

    private QualifiedName name;
    private final Block body;
    private final Modifier[] modifiers;
    private final String[] exceptions;
    private VarDecl[] params;

    public Fun(QualifiedName name, Block body, Modifier[] modifiers, String[] exceptions, VarDecl... params) {
        this.name = name;
        this.body = body;
        this.modifiers = modifiers;
        this.exceptions = exceptions;
        this.params = params;


        if (this.params == null)
            this.params = new VarDecl[0];
    }

    public QualifiedName getName() {
        return name;
    }

    public Block getBody() {
        return body;
    }

    public VarDecl[] getParams() {
        return params;
    }

    public void setName(String name) {
        this.name = new QualifiedName(name);
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.visitFun(this);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Arrays.stream(params).forEach(param -> builder.append(param).append(", "));
        return "fun " + name + "(" + builder.toString().substring(0, builder.length()) + ") " + body;
    }

    public Modifier[] getModifiers() {
        return modifiers;
    }

    public int modifiers() {
        int mod = 0;
        if (getModifiers() == null)
            return mod;
        for (Modifier modifier : getModifiers()) {
            mod += modifier.getModifier();
        }
        return mod;
    }

    public String getDesc() {
        StringBuilder desc = new StringBuilder("([Ljava/lang/String;)V");
        if (!getName().toString().equals("main")) {
            desc = new StringBuilder("(");
            for (VarDecl ignored : getParams()) {
                desc.append(Constants.TOBJ_SIG);
            }
            if (name.toString().equals("<init>")) {
                desc.append(")V");
            } else {
                desc.append(")" + Constants.TOBJ_SIG);
            }
        }
        if (getName().toString().equals("<clinit>"))
            desc = new StringBuilder("()V");

        return desc.toString();
    }

    public String[] getExceptions() {
        return exceptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fun fun = (Fun) o;
        return Objects.equals(name, fun.name) &&
                Objects.equals(body, fun.body) &&
                Arrays.equals(modifiers, fun.modifiers) &&
                Arrays.equals(exceptions, fun.exceptions) &&
                Arrays.equals(params, fun.params) &&
                Objects.equals(getAnnotations(), fun.getAnnotations());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), name, body);
        result = 31 * result + Arrays.hashCode(modifiers);
        result = 31 * result + Arrays.hashCode(exceptions);
        result = 31 * result + Arrays.hashCode(params);
        return result;
    }
}
