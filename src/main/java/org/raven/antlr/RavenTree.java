package org.raven.antlr;

import org.raven.antlr.ast.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Root of the src
 */
public class RavenTree extends Node {

    private List<Statement> statements = new ArrayList<>();
    private List<ClassDef> classes = new ArrayList<>();
    private List<Fun> functions = new ArrayList<>();
    private QualifiedName pack = new QualifiedName();
    private List<QualifiedName> imports = new ArrayList<>();
    private QualifiedName name = null;
    private String sourceFile = "";

    public RavenTree() {
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(final String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public void setPackage(final QualifiedName pack) {
        this.pack = pack;
    }

    public void addImport(final QualifiedName name) {
        imports.add(name);
    }

    public void setName(final String name) {
        this.name = new QualifiedName(name);
    }

    public QualifiedName getName() {
        return name;
    }

    public QualifiedName getPackage() {
        return pack;
    }

    public QualifiedName getFullName() {
        if (getPackage() != null) {
            return getPackage().add(getName());
        }
        return getName();
    }

    public List<QualifiedName> getImports() {
        return imports;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    public void addClass(final ClassDef def) {
        classes.add(def);
    }

    public void addStatement(final Statement statement) {
        statements.add(statement);
    }

    public void addFunction(final Fun fun) {
        functions.add(fun);
    }

    public void addFunctions(final Collection<Fun> functions) {
        this.functions.addAll(functions);
    }

    public void addStatements(final Collection<Statement> statements) {
        this.statements.addAll(statements);
    }

    public void addClasses(final Collection<ClassDef> classes) {
        this.classes.addAll(classes);
    }

    public List<ClassDef> getClasses() {
        return classes;
    }

    public List<Fun> getFunctions() {
        return functions;
    }

    public void accept(final TreeVisitor visitor) {
        for (Statement statement : statements) {
            statement.accept(visitor);
        }
    }

    public VarDecl findVar(final String name) {
        for (Statement statement : statements) {
            if (statement instanceof VarDecl) {
                VarDecl decl = (VarDecl) statement;
                if (decl.getName().toString().equals(name))
                    return decl;
            }
        }
        return null;
    }

    public Fun findFun(final String name) {
        for (Statement statement : statements) {
            if (statement instanceof Fun) {
                Fun decl = (Fun) statement;
                if (decl.getName().toString().equals(name))
                    return decl;
            }
        }
        return null;
    }
}
