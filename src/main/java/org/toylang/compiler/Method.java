package org.toylang.compiler;

import org.objectweb.asm.*;
import org.toylang.antlr.Errors;
import org.toylang.antlr.Operator;
import org.toylang.antlr.ast.*;
import org.toylang.antlr.ast.While;
import org.toylang.core.*;

import java.util.*;
import java.util.regex.Pattern;


public class Method extends MethodVisitor implements Opcodes, TreeVisitor {

    private static final LinkedList<ReflectiveMethod> reflectiveMethods = new LinkedList<>();

    private final List<Integer> lineNumbers = new ArrayList<>();

    private final ArrayList<String> locals = new ArrayList<>();
    private final MethodContext ctx;
    private final MethodVisitor mv;

    public Method(MethodContext ctx, MethodVisitor mv) {
        super(ASM5);
        this.ctx = ctx;
        this.mv = mv;
    }

    public void visitLine(Expression stmt) {
        int line = stmt.getLineNumber();
        if (line >= 0 && !lineNumbers.contains(line)) {
            visitLineNumber(line, new Label());
            lineNumbers.add(line);
        }
    }

    public void end() {
        visitMaxs(0, 0);
        visitEnd();
    }

    /**
     * @param name The name of the local var
     * @return The idx or -1 if not found
     */
    public int findLocal(String name) {
        return locals.indexOf(name);
    }

    @Override
    public void visitParameter(String s, int i) {
        mv.visitParameter(s, i);
    }

    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        return mv.visitAnnotationDefault();
    }

    @Override
    public AnnotationVisitor visitAnnotation(String s, boolean b) {
        return mv.visitAnnotation(s, b);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int i, TypePath typePath, String s, boolean b) {
        return mv.visitTypeAnnotation(i, typePath, s, b);
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int i, String s, boolean b) {
        return mv.visitParameterAnnotation(i, s, b);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        mv.visitAttribute(attribute);
    }

    @Override
    public void visitCode() {
        mv.visitCode();
    }

    @Override
    public void visitFrame(int i, int i1, Object[] objects, int i2, Object[] objects1) {
        mv.visitFrame(i, i1, objects, i2, objects1);
    }

    @Override
    public void visitInsn(int i) {
        mv.visitInsn(i);
    }

    @Override
    public void visitIntInsn(int i, int i1) {
        mv.visitIntInsn(i, i1);
    }

    @Override
    public void visitVarInsn(int i, int i1) {
        mv.visitVarInsn(i, i1);
    }

    @Override
    public void visitTypeInsn(int i, String s) {
        mv.visitTypeInsn(i, s);
    }

    @Override
    public void visitFieldInsn(int i, String s, String s1, String s2) {
        mv.visitFieldInsn(i, s, s1, s2);
    }

    @Override
    public void visitMethodInsn(int i, String s, String s1, String s2) {
        mv.visitMethodInsn(i, s, s1, s2);
    }

    @Override
    public void visitMethodInsn(int i, String s, String s1, String s2, boolean b) {
        mv.visitMethodInsn(i, s, s1, s2, b);
    }

    @Override
    public void visitInvokeDynamicInsn(String s, String s1, Handle handle, Object... objects) {
        mv.visitInvokeDynamicInsn(s, s1, handle, objects);
    }

    @Override
    public void visitJumpInsn(int i, Label label) {
        mv.visitJumpInsn(i, label);
    }

    @Override
    public void visitLabel(Label label) {
        mv.visitLabel(label);
    }

    @Override
    public void visitLdcInsn(Object o) {
        mv.visitLdcInsn(o);
    }

    @Override
    public void visitIincInsn(int i, int i1) {
        mv.visitIincInsn(i, i1);
    }

    @Override
    public void visitTableSwitchInsn(int i, int i1, Label label, Label... labels) {
        mv.visitTableSwitchInsn(i, i1, label, labels);
    }

    @Override
    public void visitLookupSwitchInsn(Label label, int[] ints, Label[] labels) {
        mv.visitLookupSwitchInsn(label, ints, labels);
    }

    @Override
    public void visitMultiANewArrayInsn(String s, int i) {
        mv.visitMultiANewArrayInsn(s, i);
    }

    @Override
    public AnnotationVisitor visitInsnAnnotation(int i, TypePath typePath, String s, boolean b) {
        return mv.visitInsnAnnotation(i, typePath, s, b);
    }

    @Override
    public void visitTryCatchBlock(Label label, Label label1, Label label2, String s) {
        mv.visitTryCatchBlock(label, label1, label2, s);
    }

    @Override
    public AnnotationVisitor visitTryCatchAnnotation(int i, TypePath typePath, String s, boolean b) {
        return mv.visitTryCatchAnnotation(i, typePath, s, b);
    }

    @Override
    public void visitLocalVariable(String s, String s1, String s2, Label label, Label label1, int i) {
        mv.visitLocalVariable(s, s1, s2, label, label1, i);
    }

    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(int i, TypePath typePath, Label[] labels, Label[] labels1, int[] ints, String s, boolean b) {
        return mv.visitLocalVariableAnnotation(i, typePath, labels, labels1, ints, s, b);
    }

    @Override
    public void visitLineNumber(int i, Label label) {
        mv.visitLineNumber(i, label);
    }

    @Override
    public void visitMaxs(int i, int i1) {
        mv.visitMaxs(i, i1);
    }

    @Override
    public void visitEnd() {
        mv.visitEnd();
    }

    @Override
    public void visitIf(If ifStatement) {
        Label else_ = new Label();
        Label end = new Label();

        ifStatement.getCondition().accept(this);
        visitMethodInsn(Opcodes.INVOKEVIRTUAL, Constants.TOYOBJ_NAME, "isTrue", "()Z", false);

        if (ifStatement.getElse() != null) {
            visitJumpInsn(Opcodes.IFEQ, else_);
            ifStatement.getBody().accept(this);
            visitJumpInsn(Opcodes.GOTO, end);

            visitLabel(else_);
            ifStatement.getElse().accept(this);
        } else {
            visitJumpInsn(Opcodes.IFEQ, end);
            ifStatement.getBody().accept(this);
        }
        visitLabel(end);
    }

    @Override
    public void visitWhile(While whileStatement) {
        Label start = new Label();
        Label conditional = new Label();
        visitJumpInsn(Opcodes.GOTO, conditional);
        visitLabel(start);


        whileStatement.getBody().accept(this);

        visitLabel(conditional);

        whileStatement.getCondition().accept(this);
        visitMethodInsn(Opcodes.INVOKEVIRTUAL, Constants.TOYOBJ_NAME, "isTrue", "()Z", false);
        visitJumpInsn(Opcodes.IFNE, start);
    }

    @Override
    public void visitReturn(Return ret) {
        visitLine(ret.getValue());
        if (ctx.getName().equals("main")) {
            if (ret.getValue() != null) {
                Errors.put("Cannot return value in main");
            }
            visitInsn(RETURN);
            return;
        }
        if (ret.getValue() != null) {
            ret.getValue().accept(this);
            visitInsn(ARETURN);
        } else {
            putNull();
            visitInsn(ARETURN);
        }
    }

    @Override
    public void visitFun(Fun fun) {
        if (ctx.getName().equals("main")) {
            locals.add("args"); // todo; convert args to list of ToyObjects
            // put toystring list at idx 1
        }
        for (VarDecl varDecl : fun.getParams()) {
            locals.add(varDecl.getName().toString());
        }
        fun.getBody().accept(this);
        Statement stmt = null;
        int idx = fun.getBody().getStatements().size() - 1;
        if (idx != -1)
            stmt = fun.getBody().getStatements().get(idx);
        if (stmt == null || !(stmt instanceof Return)) { // last stmt isnt return so add one
            if (ctx.getName().equals("main") || ctx.getName().equals("<clinit>") || ctx.getName().endsWith("<init>")) {
                visitInsn(RETURN);
            } else {
                putNull();
                visitInsn(ARETURN);
            }
        }
    }

    public void newObj(QualifiedName clazz, Call call) {
        visitLdcInsn(Type.getType("L" + (clazz.toString().replace(".", "/")) + ";"));
        visitListDef(new ListDef(call.getParams()));
        visitMethodInsn(INVOKESTATIC, Constants.TOYOBJ_NAME, "newObj", "(" + Constants.CLASS_SIG + Constants.TOYOBJ_SIG + ")" + Constants.TOYOBJ_SIG, false);
    }

    private String getPackage(String name) {
        int idx = name.lastIndexOf('.');

        while (idx != -1) {
            name = name.substring(0, idx);
            if (Package.getPackage(name) != null) {
                return name;
            }
            idx = name.lastIndexOf('.');
        }
        return null;
    }

    private String getImportedName(String name) {
        for (QualifiedName qualifiedName : ctx.getImports()) {
            if (qualifiedName.toString().endsWith(name)) {
                return qualifiedName.toString().replace(".", "/");
            }
        }
        return null;
    }

    @Override
    public void visitFunCall(Call call) {
        visitLine(call);
        StringBuilder sig = new StringBuilder("(");
        for (Expression varDecl : call.getParams()) {
            sig.append(Constants.TOYOBJ_SIG);
        }
        sig.append(")" + Constants.TOYOBJ_SIG);

        if (call.getPrecedingExpr() != null) {
            Expression expr = call.getPrecedingExpr();
            if (expr instanceof QualifiedName) {
                String pack = getPackage(expr.toString());
                String funcOwner = getImportedName(expr.toString());
                if (pack != null) {
                    // package . Class . Fun
                    // typically not imported
                } else if (funcOwner != null) { // check if its an imported name

                    String funName = call.getName().toString();//expr.toString().substring(funcOwner.length());
                    Fun f = SymbolMap.resolveFun(funcOwner, funName);
                    // if the function can be resolved, it is a toylang function
                    if (f != null) {
                        for (Expression expression : call.getParams()) {
                            expression.accept(this);
                        }
                        visitMethodInsn(INVOKESTATIC, funcOwner, funName, sig.toString(), false);
                    } else {
                        String clazz = funcOwner.replace("/", ".");
                        if(canRegisterMethod(clazz, funName, call.getParams().length)) {
                            invokeRegistered(call, funName, clazz);
                            visitMethodInsn(INVOKESTATIC, Constants.TOYOBJ_NAME, "invoke", "(ILorg/toylang/core/ToyObject;)Lorg/toylang/core/ToyObject;");
                        } else  {
                            visitLdcInsn(Type.getType("L" + (funcOwner.replace(".", "/")) + ";"));
                            visitLdcInsn(funName);
                            visitListDef(new ListDef(call.getParams()));
                            visitMethodInsn(INVOKESTATIC, Constants.TOYOBJ_NAME, "invoke", "(" + Constants.CLASS_SIG + Constants.STRING_SIG + Constants.TOYOBJ_SIG + ")" + Constants.TOYOBJ_SIG, false);
                        }
                    }
                } else {
                    //expr.accept(this);
                    //fieldOp((QualifiedName) expr, true);

                    expr.accept(this);

                    visitLdcInsn(call.getName().toString());
                    visitListDef(new ListDef(call.getParams()));
                    visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, "invoke", "(" + Constants.STRING_SIG + Constants.TOYOBJ_SIG + ")" + Constants.TOYOBJ_SIG, false);
                }
            } else {
                expr.accept(this);
                // invoke virtual
                // we should have some object on the stack
                if (call.getName().toString().equals("getField")) {
                    visitLdcInsn(call.getParams()[0].toString());
                    visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, "getField", "(" + Constants.STRING_SIG + ")" + Constants.TOYOBJ_SIG);
                } else {
                    visitLdcInsn(call.getName().toString());
                    visitListDef(new ListDef(call.getParams()));
                    visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, "invoke", "(" + Constants.STRING_SIG + Constants.TOYOBJ_SIG + ")" + Constants.TOYOBJ_SIG, false);
                }
            }
        } else {
            String clazz = getImportedName(call.getName().toString());
            if (clazz != null) {
                // invoke constructor
                newObj(new QualifiedName(clazz), call);
            } else {
                String owner = ctx.getOwner();
                if (Builtin.isBuiltin(call.getName(), call.getParams().length)) {
                    owner = Constants.BUILTIN_NAME;
                }
                for (Expression expression : call.getParams()) {
                    expression.accept(this);
                }
                visitMethodInsn(INVOKESTATIC, owner, call.getName().toString(), sig.toString(), false);
            }
        }
        if (call.pop())
            visitInsn(POP);
    }

    private void invokeRegistered(Call call, String funName, String clazz) {
        try {
            Class cl = Class.forName(clazz);
            reflectiveMethods.add(new ReflectiveMethod(cl, funName, call.getParams().length));

            visitLdcInsn(Objects.hash(cl.getName(), funName, call.getParams().length));
            visitListDef(new ListDef(call.getParams()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void visitBlock(Block block) {
        block.getStatements().forEach(stmt -> stmt.accept(this));
    }

    @Override
    public void visitVarDecl(VarDecl decl) {
        if (decl.getInitialValue() != null)
            decl.getInitialValue().accept(this);
        else
            putNull();
        if (ctx.getName().equals("<clinit>")) {
            visitFieldInsn(PUTSTATIC, ctx.getOwner(), decl.getName().toString(), Constants.TOYOBJ_SIG);
        } else {
            locals.add(decl.getName().toString());
            visitVarInsn(ASTORE, findLocal(decl.getName().toString()));
        }
    }

    @Override
    public void visitImport(Import importStatement) {

    }

    @Override
    public void visitBinOp(BinOp op) {
        if (op.getOp() != Operator.ASSIGNMENT && op.getOp() != Operator.NOT)
            op.getLeft().accept(this);

        op.getRight().accept(this);

        switch (op.getOp()) {
            case ASSIGNMENT:
                if (op.getLeft() instanceof ListIndex) {
                    assignListIdx((ListIndex) op.getLeft());
                    break;
                }
                int idx = findLocal(op.getLeft().toString());
                if (idx != -1) {
                    visitVarInsn(ASTORE, idx);
                } else {
                    fieldOp((QualifiedName) op.getLeft(), false);
                }
                break;
            case NOT:
                visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, "not", "()Lorg/toylang/core/ToyObject;", false);
                break;
            case AND:
                visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, "and", "(Lorg/toylang/core/ToyObject;)Lorg/toylang/core/ToyObject;", false);
                break;
            case OR:
                visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, "or", "(Lorg/toylang/core/ToyObject;)Lorg/toylang/core/ToyObject;", false);
                break;
            case ADD:
                visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, "add", "(Lorg/toylang/core/ToyObject;)Lorg/toylang/core/ToyObject;", false);
                break;
            case SUB:
                visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, "sub", "(Lorg/toylang/core/ToyObject;)Lorg/toylang/core/ToyObject;", false);
                break;
            case MULT:
                visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, "mul", "(Lorg/toylang/core/ToyObject;)Lorg/toylang/core/ToyObject;", false);
                break;
            case DIV:
                visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, "div", "(Lorg/toylang/core/ToyObject;)Lorg/toylang/core/ToyObject;", false);
                break;
            case MOD:
                visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, "mod", "(Lorg/toylang/core/ToyObject;)Lorg/toylang/core/ToyObject;", false);
                break;
            case EXP:
                visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, "pow", "(Lorg/toylang/core/ToyObject;)Lorg/toylang/core/ToyObject;", false);
                break;
            case GT:
                visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, "GT", "(Lorg/toylang/core/ToyObject;)Lorg/toylang/core/ToyObject;", false);
                break;
            case LT:
                visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, "LT", "(Lorg/toylang/core/ToyObject;)Lorg/toylang/core/ToyObject;", false);
                break;
            case GTE:
                visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, "GTE", "(Lorg/toylang/core/ToyObject;)Lorg/toylang/core/ToyObject;", false);
                break;
            case LTE:
                visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, "LTE", "(Lorg/toylang/core/ToyObject;)Lorg/toylang/core/ToyObject;", false);
                break;
            case EQ:
                visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, "EQ", "(Lorg/toylang/core/ToyObject;)Lorg/toylang/core/ToyObject;", false);
                break;
            case NE:
                visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, "NE", "(Lorg/toylang/core/ToyObject;)Lorg/toylang/core/ToyObject;", false);
                break;
        }
    }

    @Override
    public void visitUnaryOp(UnaryOp op) {

    }

    @Override
    public void visitLiteral(Literal literal) {
        visitLine(literal);
        ToyObject obj = (literal.getValue());
        if (obj instanceof ToyReal) {
            putReal((ToyReal) obj);
        } else if (obj instanceof ToyInt) {
            putInt((ToyInt) obj);
        } else if (obj instanceof ToyString) {
            putString((ToyString) obj);
        } else if (obj instanceof ToyBoolean) {
            putBoolean((ToyBoolean) obj);
        } else if (obj instanceof ToyNull) {
            putNull();
        }
    }

    private void getJavaObject(String owner, String name) {
        visitTypeInsn(NEW, Constants.TOYOBJ_NAME);
        visitInsn(DUP);
        visitFieldInsn(GETSTATIC, owner, name, "Ljava/lang/Object;");
        visitMethodInsn(INVOKESPECIAL, Constants.TOYOBJ_NAME, "<init>", "(Ljava/lang/Object;)V", false);
    }

    public void fieldOp(QualifiedName name, boolean load) {
        visitLine(name);

        int LOAD_OR_STORE = load ? ALOAD : ASTORE;
        int GET_OR_PUT = load ? GETSTATIC : PUTSTATIC;

        String[] names = name.getNames();
        int localIdx = findLocal(names[0]);
        if (localIdx != -1) {
            if (names.length == 1) {
                visitVarInsn(LOAD_OR_STORE, localIdx);
            } else {
                StringBuilder qname = new StringBuilder();
                for (int i = 1; i < names.length; i++) {
                    qname.append(names[i]);
                    if (i + 1 < names.length) {
                        qname.append(".");
                    }
                }
                fieldOp(new QualifiedName(names[0]), true);
                accessField(load, qname);
            }
        } else {
            String[] lst = name.getNames();
            if (lst.length == 0) {
                Errors.put("Invalid name in method: " + ctx.getOwner() + ":" + ctx.getName());
            } else if (lst.length == 1) {
                if (ctx.isStatic()) {
                    // check if it exists
                    if (ctx.findStaticVar(name.toString()) != null) {
                        visitFieldInsn(GET_OR_PUT, ctx.getOwner(), name.toString(), Constants.TOYOBJ_SIG);//todo;
                    } else {
                        Errors.put("Variable not found " + ctx.getOwner() + ":" + name.toString());
                    }
                } else {
                    VarDecl decl = ctx.getClassDef().findVar(name.toString());
                    if (decl != null) {
                        // PUSH THIS ?
                        visitVarInsn(LOAD_OR_STORE, 0);
                        int op = GETFIELD;
                        if (!load)
                            op = PUTFIELD;
                        visitFieldInsn(op, ctx.getOwner(), name.toString(), Constants.TOYOBJ_SIG);//todo;
                    }
                }
            } else {
                // ClassFile.Static
                String imp = getImportedName(names[0]);
                if (imp != null) {
                    if (load) {
                        visitLdcInsn(Type.getType("L" + (imp.replace(".", "/")) + ";"));
                        visitLdcInsn(name.getNames()[1]);
                        visitMethodInsn(INVOKESTATIC, Constants.TOYOBJ_NAME, "getField", "(" + Constants.CLASS_SIG + Constants.STRING_SIG + ")" + Constants.TOYOBJ_SIG);
                    } else {
                        visitFieldInsn(GET_OR_PUT, imp.replace(".", "/"), name.getNames()[1], Constants.TOYOBJ_SIG);
                    } // TODO TODO TODO
                    return;
                }
                VarDecl decl = ctx.findStaticVar(name.getNames()[0]);
                if (decl != null) { // static field
                    if (names.length > 1) {
                        visitFieldInsn(GETSTATIC, ctx.getOwner().replace(".", "/"), name.getNames()[1], Constants.TOYOBJ_SIG);
                        StringBuilder qname = new StringBuilder();
                        for (int i = 1; i < names.length; i++) {
                            qname.append(names[i]);
                            if (i + 1 < names.length) {
                                qname.append(".");
                            }
                        }
                        accessField(load, qname);
                    } else {
                        visitFieldInsn(GET_OR_PUT, ctx.getOwner().replace(".", "/"), name.getNames()[1], Constants.TOYOBJ_SIG);
                    }
                } else if (!ctx.isStatic()) {
                    // non static var in non static context
                    // get or put field...

                }
                Errors.put("Variable not found " + ctx.getOwner() + ":" + name.toString());
            }
        }
    }

    private void accessField(boolean load, StringBuilder qname) {
        if (load) {
            visitLdcInsn(qname.toString());
            visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, "getField", "(" + Constants.STRING_SIG + ")" + Constants.TOYOBJ_SIG);
        } else {
            visitInsn(SWAP);
            visitLdcInsn(qname.toString());
            visitInsn(SWAP);
            visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, "setField", "(" + Constants.STRING_SIG + Constants.TOYOBJ_SIG + ")" + Constants.TOYOBJ_SIG);
        }
    }

    @Override
    public void visitName(QualifiedName name) {
        fieldOp(name, true);
    }

    @Override
    public void visitListDef(ListDef def) {
        visitTypeInsn(NEW, "org/toylang/core/ToyList");
        visitInsn(DUP);
        visitMethodInsn(INVOKESPECIAL, "org/toylang/core/ToyList", "<init>", "()V", false);

        for (Expression expression : def.getExpressions()) {
            expression.accept(this);
            visitMethodInsn(INVOKEVIRTUAL, "org/toylang/core/ToyObject", "add", "(Lorg/toylang/core/ToyObject;)Lorg/toylang/core/ToyObject;", false);
        }
    }

    @Override
    public void visitListIdx(ListIndex idx) {
        visitName(idx.getName());
        for (Expression expression : idx.getIndex()) {
            expression.accept(this);
            visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, "get", "(" + Constants.TOYOBJ_SIG + ")" + Constants.TOYOBJ_SIG);
        }
    }

    private void assignListIdx(ListIndex idx) {

        visitName(idx.getName());
        for (int i = 0; i < idx.getIndex().length - 1; i++) {
            Expression expression = idx.getIndex()[i];
            expression.accept(this);
            visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, "get", "(" + Constants.TOYOBJ_SIG + ")" + Constants.TOYOBJ_SIG);
        }
        visitInsn(SWAP);
        idx.getIndex()[idx.getIndex().length - 1].accept(this);
        visitInsn(SWAP);
        visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, "set", "(" + Constants.TOYOBJ_SIG + Constants.TOYOBJ_SIG + ")" + Constants.TOYOBJ_SIG);
        visitInsn(POP);
    }

    @Override
    public void visitClassDef(ClassDef def) {
        // inner class
    }

    @Override
    public void visitDictDef(DictDef def) {
        visitTypeInsn(NEW, Constants.TOY_DICT_NAME);
        visitInsn(DUP);
        visitMethodInsn(INVOKESPECIAL, Constants.TOY_DICT_NAME, "<init>", "()V", false);
        Expression[] keys = def.getKeys();
        Expression[] values = def.getValues();
        for (int i = 0; i < keys.length; i++) {
            keys[i].accept(this);
            values[i].accept(this);
            visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, "put", "(" + Constants.TOYOBJ_SIG + Constants.TOYOBJ_SIG + ")" + Constants.TOYOBJ_SIG, false);
        }
    }
    private boolean canRegisterMethod(String clazz, String name, int paramCount) {
        try {
            Class c = Class.forName(clazz);
            ToyObject.registerMethod(c, name, paramCount);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public void registerMethods() {
        for (ReflectiveMethod reflectiveMethod : reflectiveMethods) {
            visitLdcInsn(Type.getType(reflectiveMethod.clazz));
            visitLdcInsn(reflectiveMethod.getName());
            visitLdcInsn(reflectiveMethod.getParamCount());
            visitMethodInsn(INVOKESTATIC, Constants.TOYOBJ_NAME, "registerMethod", "(Ljava/lang/Class;Ljava/lang/String;I)V", false);
        }
        reflectiveMethods.clear();
    }
    public void writeConstants() {
        visitLdcInsn(Constants.getConstantCount());
        visitTypeInsn(ANEWARRAY, Constants.TOYOBJ_NAME);
        int i = 0;
        for (ToyObject obj : Constants.getConstants()) {
            visitInsn(DUP);
            visitLdcInsn(i);
            if (obj instanceof ToyNull) {
                putNull();
            } else if (obj instanceof ToyBoolean) {
                putBoolean((ToyBoolean) obj);
            } else if (obj instanceof ToyInt) {
                putInt((ToyInt) obj);
            } else if (obj instanceof ToyString) {
                putString((ToyString) obj);
            } else if (obj instanceof ToyReal) {
                putReal((ToyReal) obj);
            } else {
                Errors.put("Unidentified Constant type");
                visitInsn(ACONST_NULL);
            }
            visitInsn(AASTORE);
            i++;
        }
        visitFieldInsn(PUTSTATIC, ctx.getOwner(), "__CONSTANTS__", "[" + Constants.TOYOBJ_SIG);
    }

    private void getConstant(ToyObject obj) {
        int idx = Constants.getConstants().indexOf(obj);
        visitFieldInsn(GETSTATIC, ctx.getOwner(), "__CONSTANTS__", "[" + Constants.TOYOBJ_SIG);

        if (idx >= 0) {
            visitLdcInsn(idx);
        } else {
            visitLdcInsn(Constants.getConstantCount());
            Constants.addConstant(obj);
        }
        visitInsn(AALOAD);
    }

    private void putNull() {
        if (ctx.getName().equals("<clinit>")) {
            visitFieldInsn(GETSTATIC, Constants.TOYNULL_NAME, "NULL", Constants.TOYNULL_SIG);
        } else {
            getConstant(ToyNull.NULL);
        }
    }

    private void putBoolean(ToyBoolean bool) {
        if (ctx.getName().equals("<clinit>")) {
            visitFieldInsn(GETSTATIC, Constants.TOY_BOOLEAN_NAME, bool.isTrue() ? "TRUE" : "FALSE", Constants.TOY_BOOLEAN_SIG);
        } else {
            getConstant(bool);
        }
    }

    private void putString(ToyString str) {
        if (ctx.getName().equals("<clinit>")) {
            visitTypeInsn(NEW, "org/toylang/core/ToyString");
            visitInsn(DUP);
            visitLdcInsn(str.toString());
            visitMethodInsn(INVOKESPECIAL, "org/toylang/core/ToyString", "<init>", "(Ljava/lang/String;)V", false);

        } else {
            getConstant(str);
        }
    }

    private void putReal(ToyReal real) {
        if (ctx.getName().equals("<clinit>")) {
            visitTypeInsn(NEW, "org/toylang/core/ToyReal");
            visitInsn(DUP);
            visitLdcInsn(real.getValue());
            visitMethodInsn(INVOKESPECIAL, "org/toylang/core/ToyReal", "<init>", "(D)V", false);
        } else {
            getConstant(real);
        }
    }

    private void putInt(ToyInt integer) {
        if (ctx.getName().equals("<clinit>")) {
            visitTypeInsn(NEW, "org/toylang/core/ToyInt");
            visitInsn(DUP);
            visitLdcInsn(integer.getValue());
            visitMethodInsn(INVOKESPECIAL, "org/toylang/core/ToyInt", "<init>", "(I)V", false);
        } else {
            getConstant(integer);
        }
    }
    static class ReflectiveMethod {
        Class clazz;
        String name;
        int paramCount;

        ReflectiveMethod(Class clazz, String name, int paramCount) {
            this.clazz = clazz;
            this.name = name;
            this.paramCount = paramCount;
        }

        public Class getClazz() {
            return clazz;
        }

        public String getName() {
            return name;
        }

        public int getParamCount() {
            return paramCount;
        }
    }
}