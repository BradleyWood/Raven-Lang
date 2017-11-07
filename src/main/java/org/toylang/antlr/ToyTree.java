package org.toylang.antlr;

import org.toylang.antlr.ast.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Root of the src
 */
public class ToyTree extends AST {

    private List<Statement> statements = new ArrayList<>();
    private QualifiedName pack = null;
    private List<QualifiedName> imports = new ArrayList<>();
    private QualifiedName name = null;

    public ToyTree() {
    }

    public ToyTree(List<Statement> statements) {
        this.statements = statements;
    }

    public void setPackage(QualifiedName pack) {
        this.pack = pack;
    }

    public void addImport(QualifiedName name) {
        imports.add(name);
        statements.add(new Import(name));
    }

    public void setName(String name) {
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

    public void accept(TreeVisitor visitor) {
        for (Statement statement : statements) {
            statement.accept(visitor);
        }
    }

    public VarDecl findVar(String name) {
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

    public Fun findFun(String name) {
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
