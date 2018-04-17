package org.raven.antlr;

import org.raven.antlr.ast.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Root of the src
 */
public class RavenTree extends AST {

    private List<Statement> statements = new ArrayList<>();
    private QualifiedName pack = new QualifiedName();
    private List<QualifiedName> imports = new ArrayList<>();
    private QualifiedName name = null;
    private String sourceFile = "";

    public RavenTree() {
    }

    public RavenTree(final List<Statement> statements) {
        this.statements = statements;
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
        statements.add(new Import(name));
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

    public List<ClassDef> getClasses() {
        List<ClassDef> classes = new ArrayList<>();
        for (Statement statement : statements) {
            if (statement instanceof ClassDef) {
                classes.add((ClassDef) statement);
            }
        }
        return classes;
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
