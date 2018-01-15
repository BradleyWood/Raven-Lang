package org.toylang.compiler;

import org.toylang.antlr.ast.*;

import java.util.List;

public class MethodContext {

    private final String owner;
    private String name;
    private final List<QualifiedName> imports;

    private boolean isStatic = false;

    private final ClassDef def;

    public MethodContext(String owner, String name, List<QualifiedName> imports, ClassDef def) {
        this.owner = owner;
        this.name = name;
        this.imports = imports;
        this.def = def;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<QualifiedName> getImports() {
        return imports;
    }

    public ClassDef getClassDef() {
        return def;
    }
}