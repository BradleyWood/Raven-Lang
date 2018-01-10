package org.toylang.compiler;

import org.toylang.antlr.ast.*;

import java.util.List;

public class MethodContext {

    private final String owner;
    private String name;
    private final List<QualifiedName> imports;
    private final List<VarDecl> staticVariables;
    private final List<Fun> staticFunctions;


    private final ClassDef def; //  if !isStatic

    public MethodContext(String owner, String name, List<QualifiedName> imports, List<VarDecl> staticVariables, List<Fun> staticFunctions) {
        this(owner, name, imports, staticVariables, staticFunctions, null);
    }

    public MethodContext(String owner, String name, List<QualifiedName> imports, List<VarDecl> staticVariables, List<Fun> staticFunctions, ClassDef def) {
        this.owner = owner;
        this.name = name;
        this.imports = imports;
        this.staticFunctions = staticFunctions;
        this.staticVariables = staticVariables;
        this.def = def;
    }

    public boolean isStatic() {
        return def == null;
    }

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VarDecl findStaticVar(String name) {
        for (VarDecl staticVariable : staticVariables) {
            if (staticVariable.getName().toString().equals(name))
                return staticVariable;
        }
        return null;
    }

    public Fun findStaticFun(String name) {
        for (Fun staticFunction : staticFunctions) {
            if (staticFunction.getName().toString().equals(name))
                return staticFunction;
        }
        return null;
    }

    public Fun findStaticFun(String name, int nParams) {
        for (Fun staticFunction : staticFunctions) {
            if (staticFunction.getParams().length == nParams && staticFunction.getName().toString().equals(name)) {
                return staticFunction;
            }
        }
        return null;
    }

    public List<QualifiedName> getImports() {
        return imports;
    }

    public List<VarDecl> getStaticVariables() {
        return staticVariables;
    }

    public List<Fun> getStaticFunctions() {
        return staticFunctions;
    }

    public ClassDef getClassDef() {
        return def;
    }
}