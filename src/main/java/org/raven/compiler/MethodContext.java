package org.raven.compiler;

import org.raven.antlr.ast.*;
import org.raven.core.wrappers.TObject;

import java.util.LinkedList;
import java.util.List;

public class MethodContext {

    private final LinkedList<Fun> syntheticFunctions = new LinkedList<>();
    private LinkedList<TObject> constants = new LinkedList<>();
    private final String owner;
    private String name;
    private final List<QualifiedName> imports;

    private boolean isStatic = false;

    private final ClassDef def;


    public MethodContext(final String owner, final String name, final List<QualifiedName> imports, final ClassDef def) {
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

    public void setStatic(final boolean isStatic) {
        this.isStatic = isStatic;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<QualifiedName> getImports() {
        return imports;
    }

    public ClassDef getClassDef() {
        return def;
    }

    public LinkedList<TObject> getConstants() {
        return constants;
    }

    public void setConstants(final LinkedList<TObject> constants) {
        this.constants = constants;
    }

    public void addSynthetic(final Fun fun) {
        syntheticFunctions.add(fun);
    }

    public List<Fun> getSyntheticFunctions() {
        return syntheticFunctions;
    }
}