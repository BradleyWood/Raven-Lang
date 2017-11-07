package org.toylang.antlr.ast;

import org.toylang.antlr.Modifier;
import org.toylang.antlr.Operator;
import org.toylang.antlr.ToyTree;
import org.toylang.core.wrappers.TNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ClassDef extends Statement {

    private final Modifier[] modifiers;
    private final QualifiedName name;
    private final QualifiedName super_;
    private final QualifiedName[] interfaces;
    private QualifiedName package_;
    private final List<Statement> statements;
    private Expression[] superParams = null;
    private List<Constructor> constructors = null;
    private List<VarDecl> varParams = new LinkedList<>();
    private ToyTree sourceTree = null;

    private List<Fun> methods = null;

    public ClassDef(Modifier[] modifiers, String name, Inheritance inh, List<Statement> statements) {
        this.statements = statements;
        this.modifiers = modifiers;
        this.interfaces = inh.getInterfaces();
        this.superParams = inh.getSuperParams();
        this.name = new QualifiedName(name);
        this.superParams = inh.getSuperParams();
        this.super_ = inh.getSuperClass();
    }

    public ClassDef(Modifier[] modifiers, QualifiedName package_, String name, QualifiedName super_, QualifiedName[] interfaces, List<Statement> statements) {
        this(modifiers, name, new Inheritance(super_, null, interfaces), statements);
        this.package_ = package_;
    }

    public boolean hasVarParams() {
        return varParams.size() > 0;
    }

    public void addVarParam(VarDecl decl) {
        varParams.add(decl);
    }

    public void setVarParams(List<VarDecl> varParams) {
        this.varParams = varParams;
    }

    public List<VarDecl> getVarParams() {
        return varParams;
    }

    public ToyTree getSourceTree() {
        return sourceTree;
    }

    public void setSourceTree(ToyTree sourceTree) {
        this.sourceTree = sourceTree;
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
        if (sourceTree != null) {
            for (QualifiedName qualifiedName : sourceTree.getImports()) {
                if (qualifiedName.toString().endsWith(super_.toString())) {
                    return qualifiedName;
                }
            }
        }
        return super_;
    }

    public List<VarDecl> getFields() {
        List<VarDecl> fields = new LinkedList<>();
        for (Statement statement : statements) {
            if (statement instanceof VarDecl) {
                fields.add((VarDecl) statement);
            }
        }
        fields.addAll(varParams);
        return fields;
    }

    public VarDecl findVar(String name) {
        for (Statement statement : getFields()) {
            VarDecl decl = (VarDecl) statement;
            if (decl.getName().toString().equals(name))
                return decl;
        }
        return null;
    }

    public List<Fun> getMethods() {
        if (methods != null)
            return methods;
        List<Fun> methods = new ArrayList<>();
        for (Statement statement : statements) {
            if (statement instanceof Fun) {
                Fun fun = (Fun) statement;
                if (!fun.getName().toString().equals(name.toString()))
                    methods.add(fun);
            }
        }
        methods.addAll(createGetters());
        methods.addAll(createSetters());
        return this.methods = methods;
    }

    public String getFullName() {
        if (package_ == null)
            return name.toString();
        return (package_.toString() + "." + name).replace(".", "/");
    }

    public String getSignature() {
        return "L" + getFullName() + ";";
    }

    public QualifiedName[] getInterfaces() {
        return interfaces;
    }

    public List<Constructor> getConstructors() {
        if (constructors != null)
            return constructors;

        constructors = new ArrayList<>();
        for (Statement stmt : statements) {
            if (stmt instanceof Constructor) {
                initFieldsInConstructor((Constructor) stmt);
                constructors.add((Constructor) stmt);
            }
        }
        statements.removeAll(constructors);

        if (superParams != null) {
            Constructor con = new Constructor(superParams);
            initFieldsInConstructor(con);
            constructors.add(con);
            varParams.clear();
            return constructors;
        }
        // create a new constructor for the class parameters
        // check if the super class has a default constructor
        // otherwise we need explicit definition of constructor
        boolean autoGenerate = hasVarParams();
        for (Constructor constructor : constructors) {
            if (constructor.getParams().length == varParams.size()) {
                autoGenerate = false;
                break;
            }
        }
        if (autoGenerate && varParams.size() > 0) {
            Constructor con = createConstructor();
            initFieldsInConstructor(con);
            constructors.add(con);
        }
        return constructors;
    }

    private void initFieldsInConstructor(Constructor c) {
        //getFields().forEach(field -> c.getBody().addBefore(new BinOp(field.getName(), Operator.ASSIGNMENT, field.getInitialValue())));
        for (VarDecl decl : getFields()) {
            BinOp bop = new BinOp(decl.getName(), Operator.ASSIGNMENT, new Literal(TNull.NULL));
            if (c.getBody() == null) {
                c.setBody(new Block());
            }
            c.getBody().addBefore(bop);
        }
    }

    private Constructor createConstructor() {
        List<VarDecl> fields = getVarParams();

        VarDecl[] params_ = new VarDecl[fields.size()];
        Block body = new Block();

        for (int i = 0; i < params_.length; i++) {
            QualifiedName funParamName = new QualifiedName(fields.get(i).getName().toString() + "_");
            params_[i] = new VarDecl(funParamName, null);
            body.append(new BinOp(fields.get(i).getName(), Operator.ASSIGNMENT, funParamName));
        }
        return new Constructor(new Modifier[]{Modifier.PUBLIC}, body, params_);
    }

    private List<Fun> createGetters() {
        LinkedList<Fun> getters = new LinkedList<>();
        for (VarDecl decl : getVarParams()) {
            Block block = new Block();
            block.append(new Return(decl.getName()));
            Fun f = new Fun(new QualifiedName("get" + decl.getName()), block, new Modifier[]{Modifier.PUBLIC}, null);
            getters.add(f);
        }
        return getters;
    }

    private List<Fun> createSetters() {
        LinkedList<Fun> setters = new LinkedList<>();
        for (VarDecl decl : getVarParams()) {
            Block block = new Block();
            VarDecl param = new VarDecl(new QualifiedName(decl.getName().toString() + "_"), null, null);
            block.append(new BinOp(decl.getName(), Operator.ASSIGNMENT, param.getName()));
            Fun f = new Fun(new QualifiedName("set" + decl.getName()), block, new Modifier[]{Modifier.PUBLIC}, null, param);
            setters.add(f);
        }
        return setters;
    }
}