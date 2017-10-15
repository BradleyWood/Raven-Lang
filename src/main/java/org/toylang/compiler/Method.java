package org.toylang.compiler;

import org.objectweb.asm.*;
import org.toylang.antlr.Errors;
import org.toylang.antlr.Operator;
import org.toylang.antlr.ast.*;
import org.toylang.core.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;

public class Method extends MethodVisitor implements TreeVisitor, Opcodes {

    private static final LinkedList<ReflectiveMethod> reflectiveMethods = new LinkedList<>();

    private final ArrayList<String> locals = new ArrayList<>();
    private final ArrayList<Integer> lineNumbers = new ArrayList<>();


    private final MethodContext ctx;

    public Method(final MethodContext ctx, final MethodVisitor mv) {
        super(ASM5, mv);
        this.ctx = ctx;
    }

    @Override
    public void visitEnd() {
        visitMaxs(0, 0);
        super.visitEnd();
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

    private void visitLine(Expression stmt) {
        int line = stmt.getLineNumber();
        if (line >= 0 && !lineNumbers.contains(line)) {
            visitLineNumber(line, new Label());
            lineNumbers.add(line);
        }
    }

    private void visitParams(VarDecl[] params) {
        if (!ctx.isStatic()) {
            locals.add("this");
        } else if (ctx.getName().equals("main")) {
            locals.add(" args ");
            if (params.length > 1) {
                Errors.put("Invalid main signature!");
            } else if (params.length == 1) {
                locals.add(params[0].getName().toString());
                visitVarInsn(ALOAD, 0);
                visitMethodInsn(INVOKESTATIC, getInternalName(ToyObject.class), "toToyLang", getDesc(ToyObject.class, "toToyLang", Object.class), false);
                visitTypeInsn(CHECKCAST, getInternalName(ToyList.class));
                visitVarInsn(ASTORE, 1);
            }
        } else if (ctx.getName().equals("<init>")) {
            visitVarInsn(ALOAD, 0);
            visitMethodInsn(INVOKESPECIAL, ctx.getClassDef().getSuper().toString().replace(".", "/"), "<init>", "()V", false);
        }
        if (ctx.getName().equals("main") && ctx.isStatic()) {
            return;
        }
        for (VarDecl varDecl : params) {
            locals.add(varDecl.getName().toString());
        }
    }

    @Override
    public void visitFun(Fun fun) {
        visitParams(fun.getParams());

        if (fun.getName().toString().equals("<clinit>")) {
            writeConstants();
            registerMethods();
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

    private String getFunDescriptor(Expression[] params) {
        StringBuilder sig = new StringBuilder("(");
        for (Expression varDecl : params) {
            sig.append(Constants.TOYOBJ_SIG);
        }
        sig.append(")" + Constants.TOYOBJ_SIG);
        return sig.toString();
    }

    @Override
    public void visitFunCall(Call call) {
        visitLine(call);

        String desc = getFunDescriptor(call.getParams());

        if (call.getPrecedingExpr() != null) {
            final Expression precedingExpr = call.getPrecedingExpr();
            if (precedingExpr instanceof QualifiedName) {
                String owner = getInternalNameFromImports(precedingExpr.toString());
                if (owner != null) {
                    resolveStaticFun(owner, call.getName().toString(), desc, call.getParams());
                } else {
                    precedingExpr.accept(this);
                    java.lang.reflect.Method method = isToyObjectFunction(call.getName().toString(), call.getParams().length);
                    if (method != null) {
                        invokeVirtualFun(call.getName().toString(), call.getParams(), method);
                    } else {
                        invokeVirtualFun(call.getName().toString(), call.getParams());
                    }
                }
            } else {
                precedingExpr.accept(this);
                java.lang.reflect.Method method = isToyObjectFunction(call.getName().toString(), call.getParams().length);
                if (method != null) {
                    invokeVirtualFun(call.getName().toString(), call.getParams(), method);
                } else {
                    invokeVirtualFun(call.getName().toString(), call.getParams());
                }
            }
        } else {
            String clazz = getInternalNameFromImports(call.getName().toString());
            if (clazz != null) {
                newObject(clazz, call.getParams());
            } else {
                String funOwner = ctx.getOwner();
                if (Builtin.isBuiltin(call.getName(), call.getParams().length)) {
                    funOwner = Constants.BUILTIN_NAME;
                }
                invokeStaticMethod_D(funOwner, call.getName().toString(), desc, call.getParams());
            }
        }

        // occurs when the result of the expression is not used
        if (call.pop()) {
            visitInsn(POP);
        }
    }

    private java.lang.reflect.Method isToyObjectFunction(String name, int paramCount) {
        for (java.lang.reflect.Method m : ToyObject.class.getDeclaredMethods()) {
            if (m.getAnnotation(Hidden.class) != null || m.getParameterCount() != paramCount)
                continue;
            if (m.getName().equals(name))
                return m;
        }
        return null;
    }

    private void resolveStaticFun(String funOwner, String funName, String desc, Expression[] params) {
        Fun f = SymbolMap.resolveFun(funOwner, funName);
        // if the function can be resolved, it is a toylang function
        if (f != null) {
            invokeStaticMethod_D(funOwner, funName, desc, params);
        } else {
            String clazz = funOwner.replace("/", ".");
            if (canRegisterMethod(clazz, funName, params.length)) {
                invokeRegistered(clazz, funName, params);
                visitMethodInsn(INVOKESTATIC, Constants.TOYOBJ_NAME, "invoke", getDesc(ToyObject.class, "invoke", int.class, ToyObject.class), false);
            } else {
                visitLdcInsn(Type.getType("L" + (funOwner.replace(".", "/")) + ";"));
                visitLdcInsn(funName);
                visitListDef(new ListDef(params));
                visitMethodInsn(INVOKESTATIC, Constants.TOYOBJ_NAME, "invoke", getDesc(ToyObject.class, "invoke", Class.class, String.class, ToyObject.class), false);
            }
        }
    }

    private void invokeStaticMethod_D(String owner, String name, String desc, Expression[] params) {
        for (Expression param : params) {
            param.accept(this);
        }
        visitMethodInsn(INVOKESTATIC, owner, name, desc, false);
    }

    private void invokeVirtualFun(String name, Expression[] params) {
        visitLdcInsn(name);
        visitListDef(new ListDef(params));
        visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, "invoke", getDesc(ToyObject.class, "invoke", String.class, ToyObject.class), false);
    }

    private void invokeVirtualFun(String name, Expression[] params, java.lang.reflect.Method method) {
        String desc = Type.getMethodDescriptor(method);
        Arrays.stream(params).forEach(expression -> expression.accept(this));
        visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, name, desc, false);
        if (method.getReturnType() == int.class) {
            visitMethodInsn(INVOKESTATIC, getInternalName(Integer.class), "valueOf", getDesc(Integer.class, "valueOf", int.class), false);
        }
        if (!method.getReturnType().isAssignableFrom(ToyObject.class)) {
            visitMethodInsn(INVOKESTATIC, getInternalName(ToyObject.class), "toToyLang", getDesc(ToyObject.class, "toToyLang", Object.class), false);
        }
    }

    private void invokeRegistered(String owner, String funName, Expression[] params) {
        try {
            Class cl = Class.forName(owner);
            reflectiveMethods.add(new ReflectiveMethod(cl, funName, params.length));

            visitLdcInsn(Objects.hash(cl.getName(), funName, params.length));
            visitListDef(new ListDef(params));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void newObject(String owner, Expression[] params) {
        visitLdcInsn(Type.getType("L" + (owner.replace(".", "/")) + ";"));
        visitListDef(new ListDef(params));
        visitMethodInsn(INVOKESTATIC, Constants.TOYOBJ_NAME, "newObj", getDesc(ToyObject.class, "newObj", Class.class, ToyObject.class), false);
    }

    private boolean canRegisterMethod(String owner, String name, int paramCount) {
        try {
            Class c = Class.forName(owner);
            ToyObject.registerMethod(c, name, paramCount);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void visitName(QualifiedName name) {
        accessField(name, true);
    }

    private void accessField(QualifiedName name, boolean load) {
        visitLine(name);

        String[] names = name.getNames();
        int localIdx = findLocal(names[0]);

        if (localIdx != -1 && !names[0].equals("this")) {
            switch (names.length) {
                case 1:
                    visitVarInsn(load ? ALOAD : ASTORE, localIdx);
                    break;
                default:
                    visitFieldInsn(GETSTATIC, ctx.getOwner().replace(".", "/"), names[1], Constants.TOYOBJ_SIG);
                    StringBuilder qname = new StringBuilder();
                    String[] nn = Arrays.copyOfRange(names, 1, names.length);
                    Arrays.stream(nn).forEach(qname::append);
                    accessField(new QualifiedName(names[0]), true);
                    accessVirtualField(qname.toString(), load);
                    break;
            }
        } else {
            String importedClass = getInternalNameFromImports(names[0]);
            VarDecl decl = ctx.findStaticVar(names[0]);
            switch (names.length) {
                case 1:
                    if (decl == null) {
                        Errors.put("Variable not found " + ctx.getOwner() + " "+ names[0]);
                    } else if (ctx.isStatic()) {
                        accessStaticField(ctx.getOwner(), decl.getName().toString(), load);
                    } else {
                        VarDecl var = ctx.getClassDef().findVar(name.toString());
                        if (var != null) {
                            accessVirtualField(var, load);
                        } else {
                            Errors.put("Variable not found " + name.toString());
                        }
                    }
                    break;
                default:
                    if (importedClass != null) {
                        accessStaticField(importedClass, names[1], load);
                    } else {
                        if (decl != null) {
                            visitFieldInsn(GETSTATIC, ctx.getOwner().replace(".", "/"), names[1], Constants.TOYOBJ_SIG);
                            StringBuilder qname = new StringBuilder();
                            String[] nn = Arrays.copyOfRange(names, 1, names.length);
                            Arrays.stream(nn).forEach(qname::append);
                            accessVirtualField(qname.toString(), load);
                        }
                    }
                    break;
            }
        }
    }

    private void accessStaticField(String owner, String name, boolean load) {
        try {
            Class clazz = Class.forName(owner.replace("/", "."));
            if (!clazz.getField(name).getType().isAssignableFrom(ToyObject.class)) {
                visitLdcInsn(Type.getType("L" + (owner) + ";"));
                visitLdcInsn(name);
                if (load) {
                    visitMethodInsn(INVOKESTATIC, getDesc(ToyObject.class), "getField", getDesc(ToyObject.class, "getField", Class.class, String.class), false);
                } else {
                    System.err.println("ERROR");
                    visitMethodInsn(INVOKESTATIC, getDesc(ToyObject.class), "getField", getDesc(ToyObject.class, "setField", Class.class, String.class, ToyObject.class), false);
                }
                return;
            }
        } catch (Exception e) {

        }
        visitFieldInsn(load ? GETSTATIC : PUTSTATIC, owner, name, getDesc(ToyObject.class));
    }

    private void accessVirtualField(VarDecl var, boolean load) {
        int idx = findLocal("this");
        if (idx != 0) {
            Errors.put("Cannot access field in non static context: " + var.getName());
            return;
        }
        visitVarInsn(ALOAD, idx);
        if (!load) {
            visitInsn(SWAP);
        }
        visitFieldInsn(load ? GETFIELD : PUTFIELD, ctx.getOwner(), var.getName().toString(), getDesc(ToyObject.class));
    }

    private void accessVirtualField(String name, boolean load) {
        if (load) {
            visitLdcInsn(name);
            visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, "getField", getDesc(ToyObject.class, "getField", String.class), false);
        } else {
            visitInsn(SWAP);
            visitLdcInsn(name);
            visitInsn(SWAP);
            visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, "setField", getDesc(ToyObject.class, "setField", String.class, ToyObject.class), false);
        }
    }

    @Override
    public void visitBlock(Block block) {
        block.getStatements().forEach(stmt -> stmt.accept(this));
    }

    @Override
    public void visitVarDecl(VarDecl decl) {
        if (decl.getInitialValue() != null) {
            decl.getInitialValue().accept(this);
        } else {
            putNull();
        }
        if (ctx.getName().equals("<clinit>")) {
            visitFieldInsn(PUTSTATIC, ctx.getOwner(), decl.getName().toString(), Constants.TOYOBJ_SIG);
        } else {
            locals.add(decl.getName().toString());
            visitVarInsn(ASTORE, findLocal(decl.getName().toString()));
        }
    }

    private int findLocal(String name) {
        return locals.indexOf(name);
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
                    accessField((QualifiedName) op.getLeft(), false);
                }
                break;
            case NOT:
                visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, op.getOp().name, getDesc(ToyObject.class, op.getOp().name), false);
                break;
            default:
                visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, op.getOp().name, getDesc(ToyObject.class, op.getOp().name, ToyObject.class), false);
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

    @Override
    public void visitListDef(ListDef def) {
        visitTypeInsn(NEW, getInternalName(ToyList.class));
        visitInsn(DUP);
        visitMethodInsn(INVOKESPECIAL, getInternalName(ToyList.class), "<init>", "()V", false);

        for (Expression expression : def.getExpressions()) {
            expression.accept(this);
            visitMethodInsn(INVOKEVIRTUAL, getInternalName(ToyObject.class), "add", getDesc(ToyList.class, "add", ToyObject.class), false);
        }
    }

    @Override
    public void visitListIdx(ListIndex idx) {
        visitName(idx.getName());
        for (Expression expression : idx.getIndex()) {
            expression.accept(this);
            visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, "get", getDesc(ToyObject.class, "get", ToyObject.class), false);
        }
    }

    @Override
    public void visitClassDef(ClassDef def) {
        // inner class
    }

    @Override
    public void visitDictDef(DictDef def) {
        visitTypeInsn(NEW, getInternalName(ToyDict.class));
        visitInsn(DUP);
        visitMethodInsn(INVOKESPECIAL, getInternalName(ToyDict.class), "<init>", "()V", false);
        Expression[] keys = def.getKeys();
        Expression[] values = def.getValues();
        for (int i = 0; i < keys.length; i++) {
            keys[i].accept(this);
            values[i].accept(this);
            visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, "put", getDesc(ToyDict.class, "put", ToyObject.class, ToyObject.class), false);
        }
    }

    @Override
    public void visitAnnotation(Annotation annotation) {
        System.err.println("Annotations are not implemented");
    }

    private void assignListIdx(ListIndex idx) {
        visitName(idx.getName());
        for (int i = 0; i < idx.getIndex().length - 1; i++) {
            Expression expression = idx.getIndex()[i];
            expression.accept(this);
            visitMethodInsn(INVOKEVIRTUAL, getInternalName(ToyObject.class), "get", getDesc(ToyObject.class, "get", ToyObject.class), false);
        }
        visitInsn(SWAP);
        idx.getIndex()[idx.getIndex().length - 1].accept(this);
        visitInsn(SWAP);
        visitMethodInsn(INVOKEVIRTUAL, Constants.TOYOBJ_NAME, "set", getDesc(ToyObject.class, "set", ToyObject.class, ToyObject.class), false);
        visitInsn(POP);
    }

    private void registerMethods() {
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
        visitFieldInsn(PUTSTATIC, ctx.getOwner(), "__CONSTANTS__", getDesc(ToyObject[].class));
    }

    private void getConstant(ToyObject obj) {
        int idx = Constants.getConstants().indexOf(obj);
        visitFieldInsn(GETSTATIC, ctx.getOwner(), "__CONSTANTS__", getDesc(ToyObject[].class));

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
            visitFieldInsn(GETSTATIC, getInternalName(ToyNull.NULL), "NULL", getDesc(ToyNull.NULL));
        } else {
            getConstant(ToyNull.NULL);
        }
    }

    private void putBoolean(ToyBoolean bool) {
        if (ctx.getName().equals("<clinit>")) {
            visitFieldInsn(GETSTATIC, getInternalName(bool), bool.isTrue() ? "TRUE" : "FALSE", getDesc(bool));
        } else {
            getConstant(bool);
        }
    }

    private void putString(ToyString str) {
        if (ctx.getName().equals("<clinit>")) {
            visitTypeInsn(NEW, getInternalName(str));
            visitInsn(DUP);
            visitLdcInsn(str.toString());
            visitMethodInsn(INVOKESPECIAL, getInternalName(str), "<init>", "(Ljava/lang/String;)V", false);
        } else {
            getConstant(str);
        }
    }

    private void putReal(ToyReal real) {
        if (ctx.getName().equals("<clinit>")) {
            visitTypeInsn(NEW, getInternalName(real));
            visitInsn(DUP);
            visitLdcInsn(real.getValue());
            visitMethodInsn(INVOKESPECIAL, getInternalName(real), "<init>", "(D)V", false);
        } else {
            getConstant(real);
        }
    }

    private void putInt(ToyInt integer) {
        if (ctx.getName().equals("<clinit>")) {
            visitTypeInsn(NEW, getInternalName(integer));
            visitInsn(DUP);
            visitLdcInsn(integer.getValue());
            visitMethodInsn(INVOKESPECIAL, getInternalName(integer), "<init>", "(I)V", false);
        } else {
            getConstant(integer);
        }
    }

    private String getInternalName(Object obj) {
        return Type.getType(obj.getClass()).getInternalName();
    }

    private String getDesc(Object obj) {
        return Type.getType(obj.getClass()).getDescriptor();
    }

    private String getInternalName(Class c) {
        return Type.getType(c).getInternalName();
    }

    private String getDesc(Class c) {
        return Type.getType(c).getDescriptor();
    }

    private String getDesc(java.lang.reflect.Method m) {
        return Type.getMethodDescriptor(m);
    }

    private String getDesc(Class<?> c, String methodName, Class... params) {
        try {
            return Type.getMethodDescriptor(c.getMethod(methodName, params));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Errors.put("Cannot find builtin method: " + c.getName() + "." + methodName);
        }
        return null;
    }

    private String getInternalNameFromImports(String name) {
        for (QualifiedName qualifiedName : ctx.getImports()) {
            if (qualifiedName.toString().endsWith(name)) {
                return qualifiedName.toString().replace(".", "/");
            }
        }
        return null;
    }

    private static class ReflectiveMethod {
        private Class clazz;
        private String name;
        private int paramCount;

        private ReflectiveMethod(Class clazz, String name, int paramCount) {
            this.clazz = clazz;
            this.name = name;
            this.paramCount = paramCount;
        }

        private Class getClazz() {
            return clazz;
        }

        private String getName() {
            return name;
        }

        private int getParamCount() {
            return paramCount;
        }
    }
}
