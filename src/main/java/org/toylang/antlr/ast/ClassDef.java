package org.toylang.antlr.ast;

import org.toylang.antlr.Modifier;
import org.toylang.antlr.Operator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClassDef extends Statement {

    private final Modifier[] modifiers;
    private final QualifiedName name;
    private final QualifiedName super_;
    private final String[] interfaces;
    private QualifiedName package_;
    private final List<Statement> statements;
    private List<Fun> constructors = null;

    public ClassDef(Modifier[] modifiers, String name, QualifiedName super_, String[] interfaces, List<Statement> statements) {
        this.statements = statements;
        this.modifiers = modifiers;
        this.interfaces = interfaces;
        this.name = new QualifiedName(name);
        this.super_ = super_;
    }
    public ClassDef(Modifier[] modifiers, QualifiedName package_, String name, QualifiedName super_, String[] interfaces, List<Statement> statements) {
        this(modifiers, name, super_, interfaces, statements);
        this.package_ = package_;
    }
    @Override
    public void accept(TreeVisitor visitor) {
        visitor.visitClassDef(this);
    }
    public QualifiedName getPackage() {
        return package_;
    }
    public void setPackage(QualifiedName package_) {
        this.package_ = package_;
    }
    public List<Statement> getStatements() {
        return statements;
    }
    public Modifier[] getModifiers() {
        return modifiers;
    }
    public QualifiedName getName() {
        return name;
    }
    public QualifiedName getSuper() {
        return super_;
    }
    public List<Statement> getFields() {
        return statements.stream().filter(stmt -> (stmt instanceof VarDecl)).collect(Collectors.toList());
    }
    public VarDecl findVar(String name) {
        for (Statement statement : getFields()) {
            VarDecl decl = (VarDecl) statement;
            if(decl.getName().toString().equals(name))
                return decl;
        }
        return null;
    }
    public List<Fun> getMethods() {
        List<Fun> methods = new ArrayList<>();
        for (Statement statement : statements) {
            if(statement instanceof Fun) {
                Fun fun = (Fun)statement;
                if(!fun.getName().toString().equals(name.toString()))
                    methods.add(fun);
            }
        }
        return methods;
    }
    public String getFullName() {
        if(package_ == null)
            return name.toString();
        return (package_.toString() + "."+ name).replace(".", "/");
    }
    public String getSignature() {
        return "L" + getFullName() + ";";
    }
    public String[] getInterfaces() {
        return interfaces;
    }
    public List<Fun> getConstructors() {
        if(constructors != null)
            return constructors;

        constructors = new ArrayList<>();
        for(Statement stmt : statements) {
            if(stmt instanceof Fun) {
                Fun fun = (Fun)stmt;
                if(fun.getName().toString().equals(name.toString())) {
                    fun.setName("<init>");
                    constructors.add(fun);
                }
            }
        }
        // create a new constructor for the class parameters
        // check if the super class has a default constructor
        // otherwise we need explicit definition of constructor
        boolean autoGenerate = true;
        int fieldCount = getFields().size();
        for (Fun constructor : constructors) {
            if(constructor.getParams().length == fieldCount)
                autoGenerate = false;
        }
        if(autoGenerate && fieldCount > 0) {
            Fun con = createConstructor();
            constructors.add(con);
        }
        return constructors;
    }
    private Fun createConstructor() {
        List<Statement> fields = getFields();

        VarDecl[] params_ = new VarDecl[fields.size()];
        Block body = new Block();

        for(int i = 0; i < params_.length; i++) {
            QualifiedName funParamName = new QualifiedName(((VarDecl)fields.get(i)).getName().toString()+"_");
            params_[i] = new VarDecl(funParamName, null);
            body.append(new BinOp(((VarDecl)fields.get(i)).getName(), Operator.ASSIGNMENT, funParamName));
        }
        return new Fun(new QualifiedName("<init>"), body, new Modifier[] {Modifier.PUBLIC}, new String[0], params_);
    }
}