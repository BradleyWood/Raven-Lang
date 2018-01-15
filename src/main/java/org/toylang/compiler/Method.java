package org.toylang.compiler;

import org.objectweb.asm.*;
import org.toylang.antlr.Modifier;
import org.toylang.antlr.Operator;
import org.toylang.antlr.ast.*;
import org.toylang.core.*;
import org.toylang.core.wrappers.*;

import java.util.*;

public class Method extends MethodVisitor implements TreeVisitor, Opcodes {

    private static final LinkedList<ReflectiveMethod> reflectiveMethods = new LinkedList<>();

    private final ArrayList<Integer> lineNumbers = new ArrayList<>();

    private final Stack<Label> continueLabels = new Stack<>();
    private final Stack<Label> breakLabels = new Stack<>();

    final Scope scope = new Scope();


    protected final MethodContext ctx;

    Method(final MethodContext ctx, final MethodVisitor mv) {
        super(ASM5, mv);
        this.ctx = ctx;
    }

    int getLocal(String name) {
        int idx = scope.findVar(name);
        if (idx == -1) {
            Errors.put("Use of variable: " + name + " before it is defined");
            return 0;
        }
        return idx;
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
        visitMethodInsn(Opcodes.INVOKEVIRTUAL, Constants.TOBJ_NAME, "isTrue", "()Z", false);

        if (ifStatement.getElse() != null) {
            visitJumpInsn(Opcodes.IFEQ, else_);
            scope.beginScope();
            ifStatement.getBody().accept(this);
            scope.endScope();
            visitJumpInsn(Opcodes.GOTO, end);

            visitLabel(else_);
            scope.beginScope();
            ifStatement.getElse().accept(this);
            scope.endScope();
        } else {
            visitJumpInsn(Opcodes.IFEQ, end);
            scope.beginScope();
            ifStatement.getBody().accept(this);
            scope.endScope();
        }
        visitLabel(end);
    }

    @Override
    public void visitFor(For forStatement) {
        Label start = new Label();
        Label conditional = new Label();
        Label after = new Label();
        Label end = new Label();

        continueLabels.push(after);
        breakLabels.push(end);

        scope.beginScope();

        if (forStatement.getInit() instanceof VarDecl) {
            VarDecl decl = (VarDecl) forStatement.getInit();
            int idx = scope.findVar(decl.getName().toString());
            if (idx >= 0) {
                decl.getInitialValue().accept(this);
                visitVarInsn(ASTORE, idx);
            } else {
                forStatement.getInit().accept(this);
            }
        } else {
            forStatement.getInit().accept(this);
        }

        visitJumpInsn(GOTO, conditional);
        visitLabel(start);

        forStatement.getBody().accept(this);

        visitLabel(after);
        forStatement.getAfter().accept(this);

        visitLabel(conditional);

        forStatement.getCondition().accept(this);
        visitMethodInsn(Opcodes.INVOKEVIRTUAL, Constants.TOBJ_NAME, "isTrue", "()Z", false);
        visitJumpInsn(Opcodes.IFNE, start);
        visitLabel(end);

        continueLabels.pop();
        breakLabels.pop();

        scope.endScope();
    }

    @Override
    public void visitWhile(While whileStatement) {
        Label start = new Label();
        Label conditional = new Label();
        Label end = new Label();

        continueLabels.push(conditional);
        breakLabels.push(end);

        scope.beginScope();

        if (!whileStatement.isDoWhile())
            visitJumpInsn(GOTO, conditional);

        visitLabel(start);

        whileStatement.getBody().accept(this);

        visitLabel(conditional);

        whileStatement.getCondition().accept(this);
        visitMethodInsn(Opcodes.INVOKEVIRTUAL, Constants.TOBJ_NAME, "isTrue", "()Z", false);
        visitJumpInsn(Opcodes.IFNE, start);
        visitLabel(end);

        continueLabels.pop();
        breakLabels.pop();

        scope.endScope();
    }

    @Override
    public void visitReturn(Return ret) {
        if (ret.getValue() != null)
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
            scope.putVar("this");
        } else if (ctx.getName().equals("main")) {
            scope.putVar(" args ");
            if (params.length > 1) {
                Errors.put("Invalid main signature!");
            } else if (params.length == 1) {
                scope.putVar(params[0].getName().toString());
                visitVarInsn(ALOAD, 0);
                visitMethodInsn(INVOKESTATIC, getInternalName(TObject.class), "toToyLang", getDesc(TObject.class, "toToyLang", Object.class), false);
                visitTypeInsn(CHECKCAST, getInternalName(TList.class));
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
            scope.putVar(varDecl.getName().toString());
        }
    }

    @Override
    public void visitFun(Fun fun) {
        scope.beginScope();
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
        scope.endScope();
    }

    private String getFunDescriptor(Expression[] params) {
        StringBuilder sig = new StringBuilder("(");
        for (Expression ignored : params) {
            sig.append(Constants.TOBJ_SIG);
        }
        sig.append(")" + Constants.TOBJ_SIG);
        return sig.toString();
    }

    int counter = 0;

    @Override
    public void visitGo(Go go) {
        Call c = go.getGoFun();

        String lambdaName = "lambda$" + ctx.getName() + "$" + counter++;
        String desc = getFunDescriptor(c.getParams());
        StringBuilder params = new StringBuilder();
        for (int i = 0; i < c.getParams().length; i++) {
            params.append(getDesc(TObject.class));
        }
        visitTypeInsn(NEW, "java/lang/Thread");
        visitInsn(DUP);
        Arrays.stream(c.getParams()).forEach(a -> a.accept(this));
        visitInvokeDynamicInsn("run", "(" + params + ")Ljava/lang/Runnable;", new Handle(Opcodes.H_INVOKESTATIC,
                        "java/lang/invoke/LambdaMetafactory", "metafactory",
                        "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;"),
                Type.getType("()V"), new Handle(Opcodes.H_INVOKESTATIC, ctx.getOwner(), lambdaName, desc),
                Type.getType("()V"));
        visitMethodInsn(INVOKESPECIAL, "java/lang/Thread", "<init>", "(Ljava/lang/Runnable;)V", false);
        visitMethodInsn(INVOKEVIRTUAL, "java/lang/Thread", "start", "()V", false);
    }

    @Override
    public void visitContinue() {
        if (continueLabels.size() == 0) {
            Errors.put("Continue not allowed here");
        } else {
            visitJumpInsn(GOTO, continueLabels.peek());
        }
    }

    @Override
    public void visitBreak() {
        if (breakLabels.size() == 0) {
            Errors.put("Break not allowed here");
        } else {
            visitJumpInsn(GOTO, breakLabels.peek());
        }
    }

    @Override
    public void visitFunCall(Call call) {
        visitLine(call);

        String desc = getFunDescriptor(call.getParams());

        if (call.getName().toString().equals("super")) {
            Errors.put(ctx.getOwner() + " line " + call.getLineNumber() + ": Super() not allowed here.");
        } else if (call.getName().toString().equals("this")) {
            Errors.put(ctx.getOwner() + " line " + call.getLineNumber() + ": This() not allowed here.");
        }

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
                    invokeStaticMethod_D(funOwner, call.getName().toString(), desc, call.getParams());
                } else {
                    if (ctx.isStatic()) {
                        invokeStaticMethod_D(funOwner, call.getName().toString(), desc, call.getParams());
                    } else {
                        if (ctx.getClassDef().findFun(call.getName().toString(), call.getParams().length) != null) {
                            visitVarInsn(ALOAD, getLocal("this"));
                            Arrays.stream(call.getParams()).forEach(param -> param.accept(this));
                            visitMethodInsn(INVOKEVIRTUAL, funOwner, call.getName().toString(), desc, false);
                        } else {
                            visitLdcInsn(Type.getType("L" + (funOwner.replace(".", "/")) + ";"));
                            visitVarInsn(ALOAD, getLocal("this"));
                            visitLdcInsn(call.getName().toString());
                            visitListDef(new ListDef(call.getParams()));
                            visitMethodInsn(INVOKESTATIC, Constants.TOBJ_NAME, "invoke", getDesc(TObject.class, "invoke", Class.class, Object.class, String.class, TObject.class), false);
                        }
                    }
                }
            }
        }

        // occurs when the result of the expression is not used
        if (call.pop()) {
            visitInsn(POP);
        }
    }

    private java.lang.reflect.Method isToyObjectFunction(String name, int paramCount) {
        for (java.lang.reflect.Method m : TObject.class.getDeclaredMethods()) {
            if (m.getAnnotation(Hidden.class) != null || m.getParameterCount() != paramCount)
                continue;
            if (m.getName().equals(name))
                return m;
        }
        return null;
    }

    private void resolveStaticFun(String funOwner, String funName, String desc, Expression[] params) {
        Fun f = SymbolMap.resolveFun(ctx.getOwner(), funOwner, funName, params.length);
        // if the function can be resolved, it is a tl function
        if (f != null && !f.isJavaMethod()) {
            invokeStaticMethod_D(funOwner, funName, desc, params);
        } else {
            String clazz = funOwner.replace("/", ".");
            if (canRegisterMethod(clazz, funName, params.length)) {
                invokeRegistered(clazz, funName, params);
                visitMethodInsn(INVOKESTATIC, Constants.TOBJ_NAME, "invoke", getDesc(TObject.class, "invoke", int.class, TObject.class), false);
            } else {
                visitLdcInsn(Type.getType("L" + (funOwner.replace(".", "/")) + ";"));
                visitLdcInsn(funName);
                visitListDef(new ListDef(params));
                visitMethodInsn(INVOKESTATIC, Constants.TOBJ_NAME, "invoke", getDesc(TObject.class, "invoke", Class.class, String.class, TObject.class), false);
            }
        }
    }

    private void invokeStaticMethod_D(String owner, String name, String desc, Expression[] params) {
        Fun f = SymbolMap.resolveFun(ctx.getOwner(), owner, name, params.length);
        if (f == null && !owner.equals(Constants.BUILTIN_NAME)) {
            Errors.put(ctx.getOwner() + " Cannot resolve function: " + owner + "." + name);
            //throw new RuntimeException();
        }
        for (Expression param : params) {
            param.accept(this);
        }
        visitMethodInsn(INVOKESTATIC, owner, name, desc, false);
    }

    private void invokeVirtualFun(String name, Expression[] params) {
        visitLdcInsn(name);
        visitListDef(new ListDef(params));
        visitMethodInsn(INVOKEVIRTUAL, Constants.TOBJ_NAME, "invoke", getDesc(TObject.class, "invoke", String.class, TObject.class), false);
    }

    private void invokeVirtualFun(String name, Expression[] params, java.lang.reflect.Method method) {
        String desc = Type.getMethodDescriptor(method);
        Arrays.stream(params).forEach(expression -> expression.accept(this));
        visitMethodInsn(INVOKEVIRTUAL, Constants.TOBJ_NAME, name, desc, false);
        if (method.getReturnType() == int.class) {
            visitMethodInsn(INVOKESTATIC, getInternalName(Integer.class), "valueOf", getDesc(Integer.class, "valueOf", int.class), false);
        }
        if (!method.getReturnType().isAssignableFrom(TObject.class)) {
            visitMethodInsn(INVOKESTATIC, getInternalName(TObject.class), "toToyLang", getDesc(TObject.class, "toToyLang", Object.class), false);
        }
    }

    private void invokeRegistered(String owner, String funName, Expression[] params) {
        try {
            Class cl = Class.forName(owner);
            ReflectiveMethod method = new ReflectiveMethod(cl, funName, params.length);
            if (!reflectiveMethods.contains(method)) {
                reflectiveMethods.add(method);
            }
            visitLdcInsn(Objects.hash(cl.getName(), funName, params.length));
            visitListDef(new ListDef(params));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void newObject(String owner, Expression[] params) {
        visitLdcInsn(Type.getType("L" + (owner.replace(".", "/")) + ";"));
        visitListDef(new ListDef(params));
        visitMethodInsn(INVOKESTATIC, Constants.TOBJ_NAME, "newObj", getDesc(TObject.class, "newObj", Class.class, TObject.class), false);
    }

    private boolean canRegisterMethod(String owner, String name, int paramCount) {
        try {
            Class c = Class.forName(owner);
            TObject.registerMethod(c, name, paramCount);
            return true;
        } catch (Exception e) {
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
        int localIdx = scope.findVar(names[0]);

        if (localIdx != -1 && !names[0].equals("this")) {
            switch (names.length) {
                case 1:
                    visitVarInsn(load ? ALOAD : ASTORE, localIdx);
                    break;
                default:
                    visitFieldInsn(GETSTATIC, ctx.getOwner().replace(".", "/"), names[1], Constants.TOBJ_SIG);
                    StringBuilder qname = new StringBuilder();
                    String[] nn = Arrays.copyOfRange(names, 1, names.length);
                    Arrays.stream(nn).forEach(qname::append);
                    accessField(new QualifiedName(names[0]), true);
                    accessVirtualField(qname.toString(), load);
                    break;
            }
        } else {
            String importedClass = getInternalNameFromImports(names[0]);
            VarDecl decl = SymbolMap.resolveField(ctx.getOwner(), ctx.getOwner(), names[0]);

            switch (names.length) {
                case 1:
                    if (ctx.isStatic()) {
                        if (decl != null && decl.hasModifier(Modifier.STATIC)) {
                            accessStaticField(ctx.getOwner(), decl.getName().toString(), load);
                        } else {
                            visitFieldInsn(load ? GETSTATIC : PUTSTATIC, ctx.getOwner(), names[0], getDesc(TObject.class));
                            // check parent for field in the future
                        }
                    } else {
                        VarDecl var = ctx.getClassDef().findVar(name.toString());
                        if (var != null) {
                            accessVirtualField(var, load);
                        } else {
                            visitVarInsn(ALOAD, getLocal("this"));
                            if (!load) {
                                visitInsn(SWAP);
                                visitLdcInsn(names[0]);
                                visitInsn(SWAP);
                                visitMethodInsn(INVOKESTATIC, getInternalName(TObject.class), "setField", getDesc(TObject.class, "setField", Object.class, String.class, TObject.class), false);
                            } else {
                                visitLdcInsn(names[0]);
                                visitMethodInsn(INVOKESTATIC, getInternalName(TObject.class), "getField", getDesc(TObject.class, "getField", Object.class, String.class), false);
                            }
                            Warning.put("Warning: Variable may not exist: " + ctx.getClassDef().getName().toString() + ":" + name.toString());
                            // var could exist in super class
                        }
                    }
                    break;
                default:
                    if (importedClass != null) {
                        accessStaticField(importedClass, names[1], load);
                    } else {
                        if (decl != null) {
                            visitFieldInsn(GETSTATIC, ctx.getOwner().replace(".", "/"), names[1], Constants.TOBJ_SIG);
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
        VarDecl decl = SymbolMap.resolveField(ctx.getOwner(), owner, name);

        if (decl == null) {
            System.err.println(owner + " :: " + owner + " : " + name);
            Errors.put("Cannot resolve field " + owner + " : " + name);
            return;
        }

        if (decl.isJavaField()) {
            visitLdcInsn(Type.getType("L" + (owner) + ";"));
            visitLdcInsn(name);
            if (load) {
                visitMethodInsn(INVOKESTATIC, getInternalName(TObject.class), "getField", getDesc(TObject.class, "getField", Class.class, String.class), false);
            } else {
                Warning.put("ERROR");
                visitMethodInsn(INVOKESTATIC, getInternalName(TObject.class), "setField", getDesc(TObject.class, "setField", Class.class, String.class, TObject.class), false);
            }
            return;
        }
        visitFieldInsn(load ? GETSTATIC : PUTSTATIC, owner, name, getDesc(TObject.class));
    }

    private void accessVirtualField(VarDecl var, boolean load) {
        int idx = getLocal("this");
        if (idx != 0) {
            Errors.put(ctx.getOwner() + " line " + var.getName().getLineNumber() + ":Cannot access field in non static context: " + var.getName());
            return;
        }
        visitVarInsn(ALOAD, idx);
        if (!load) {
            visitInsn(SWAP);
        }
        visitFieldInsn(load ? GETFIELD : PUTFIELD, ctx.getOwner(), var.getName().toString(), getDesc(TObject.class));
    }

    private void accessVirtualField(String name, boolean load) {
        if (load) {
            visitLdcInsn(name);
            visitMethodInsn(INVOKEVIRTUAL, Constants.TOBJ_NAME, "getField", getDesc(TObject.class, "getField", String.class), false);
        } else {
            visitInsn(SWAP);
            visitLdcInsn(name);
            visitInsn(SWAP);
            visitMethodInsn(INVOKEVIRTUAL, Constants.TOBJ_NAME, "setField", getDesc(TObject.class, "setField", String.class, TObject.class), false);
        }
    }

    @Override
    public void visitBlock(Block block) {
        scope.beginScope();
        block.getStatements().forEach(stmt -> stmt.accept(this));
        scope.endScope();
    }

    @Override
    public void visitVarDecl(VarDecl decl) {
        if (decl.getInitialValue() != null) {
            decl.getInitialValue().accept(this);
        } else {
            putNull();
        }
        if (ctx.getName().equals("<clinit>")) {
            visitFieldInsn(PUTSTATIC, ctx.getOwner(), decl.getName().toString(), Constants.TOBJ_SIG);
        } else {
            scope.putVar(decl.getName().toString());
            visitVarInsn(ASTORE, getLocal(decl.getName().toString()));
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
                int idx = scope.findVar(op.getLeft().toString());
                if (idx != -1) {
                    visitVarInsn(ASTORE, idx);
                } else {
                    accessField((QualifiedName) op.getLeft(), false);
                }
                break;
            case NOT:
                visitMethodInsn(INVOKEVIRTUAL, Constants.TOBJ_NAME, op.getOp().name, getDesc(TObject.class, op.getOp().name), false);
                break;
            default:
                visitMethodInsn(INVOKEVIRTUAL, Constants.TOBJ_NAME, op.getOp().name, getDesc(TObject.class, op.getOp().name, TObject.class), false);
                break;
        }
    }

    @Override
    public void visitUnaryOp(UnaryOp op) {

    }

    @Override
    public void visitLiteral(Literal literal) {
        visitLine(literal);
        TObject obj = (literal.getValue());
        if (obj instanceof TReal) {
            putReal((TReal) obj);
        } else if (obj instanceof TInt) {
            putInt((TInt) obj);
        } else if (obj instanceof TString) {
            putString((TString) obj);
        } else if (obj instanceof TBoolean) {
            putBoolean((TBoolean) obj);
        } else if (obj instanceof TNull) {
            putNull();
        } else if (obj instanceof TBigInt) {
            putBigInt((TBigInt) literal.getValue());
        }
    }

    @Override
    public void visitListDef(ListDef def) {
        visitTypeInsn(NEW, getInternalName(TList.class));
        visitInsn(DUP);
        visitMethodInsn(INVOKESPECIAL, getInternalName(TList.class), "<init>", "()V", false);

        for (Expression expression : def.getExpressions()) {
            expression.accept(this);
            visitMethodInsn(INVOKEVIRTUAL, getInternalName(TObject.class), "add", getDesc(TList.class, "add", TObject.class), false);
        }
    }

    @Override
    public void visitListIdx(ListIndex idx) {
        visitName(idx.getName());
        for (Expression expression : idx.getIndex()) {
            expression.accept(this);
            visitMethodInsn(INVOKEVIRTUAL, Constants.TOBJ_NAME, "get", getDesc(TObject.class, "get", TObject.class), false);
        }
    }

    @Override
    public void visitClassDef(ClassDef def) {
        // inner class
    }

    @Override
    public void visitDictDef(DictDef def) {
        visitTypeInsn(NEW, getInternalName(TDict.class));
        visitInsn(DUP);
        visitMethodInsn(INVOKESPECIAL, getInternalName(TDict.class), "<init>", "()V", false);
        Expression[] keys = def.getKeys();
        Expression[] values = def.getValues();
        for (int i = 0; i < keys.length; i++) {
            keys[i].accept(this);
            values[i].accept(this);
            visitMethodInsn(INVOKEVIRTUAL, Constants.TOBJ_NAME, "put", getDesc(TDict.class, "put", TObject.class, TObject.class), false);
        }
    }

    @Override
    public void visitAnnotation(Annotation annotation) {
        Warning.put("Annotations are not implemented");
    }

    @Override
    public void visitAnnotationDef(AnnoDef def) {

    }

    private void assignListIdx(ListIndex idx) {
        visitName(idx.getName());
        for (int i = 0; i < idx.getIndex().length - 1; i++) {
            Expression expression = idx.getIndex()[i];
            expression.accept(this);
            visitMethodInsn(INVOKEVIRTUAL, getInternalName(TObject.class), "get", getDesc(TObject.class, "get", TObject.class), false);
        }
        visitInsn(SWAP);
        idx.getIndex()[idx.getIndex().length - 1].accept(this);
        visitInsn(SWAP);
        visitMethodInsn(INVOKEVIRTUAL, Constants.TOBJ_NAME, "set", getDesc(TObject.class, "set", TObject.class, TObject.class), false);
        visitInsn(POP);
    }

    private void registerMethods() {
        for (ReflectiveMethod reflectiveMethod : reflectiveMethods) {
            visitLdcInsn(Type.getType(reflectiveMethod.clazz));
            visitLdcInsn(reflectiveMethod.getName());
            visitLdcInsn(reflectiveMethod.getParamCount());
            visitMethodInsn(INVOKESTATIC, Constants.TOBJ_NAME, "registerMethod", "(Ljava/lang/Class;Ljava/lang/String;I)V", false);
        }
        reflectiveMethods.clear();
    }

    private void writeConstants() {
        visitLdcInsn(Constants.getConstantCount());
        visitTypeInsn(ANEWARRAY, Constants.TOBJ_NAME);
        int i = 0;
        for (TObject obj : Constants.getConstants()) {
            visitInsn(DUP);
            visitLdcInsn(i);
            if (obj instanceof TNull) {
                putNull();
            } else if (obj instanceof TBoolean) {
                putBoolean((TBoolean) obj);
            } else if (obj instanceof TInt) {
                putInt((TInt) obj);
            } else if (obj instanceof TString) {
                putString((TString) obj);
            } else if (obj instanceof TReal) {
                putReal((TReal) obj);
            } else if (obj instanceof TBigInt) {
                putBigInt((TBigInt) obj);
            } else {
                Errors.put("Unidentified Constant type");
                visitInsn(ACONST_NULL);
            }
            visitInsn(AASTORE);
            i++;
        }
        visitFieldInsn(PUTSTATIC, ctx.getOwner(), "__CONSTANTS__", getDesc(TObject[].class));
    }

    private void getConstant(TObject obj) {
        int idx = Constants.getConstants().indexOf(obj);
        visitFieldInsn(GETSTATIC, ctx.getOwner(), "__CONSTANTS__", getDesc(TObject[].class));

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
            visitFieldInsn(GETSTATIC, getInternalName(TNull.NULL), "NULL", getDesc(TNull.NULL));
        } else {
            getConstant(TNull.NULL);
        }
    }

    private void putBoolean(TBoolean bool) {
        if (ctx.getName().equals("<clinit>")) {
            visitFieldInsn(GETSTATIC, getInternalName(bool), bool.isTrue() ? "TRUE" : "FALSE", getDesc(bool));
        } else {
            getConstant(bool);
        }
    }

    private void putString(TString str) {
        if (ctx.getName().equals("<clinit>")) {
            visitTypeInsn(NEW, getInternalName(str));
            visitInsn(DUP);
            visitLdcInsn(str.toString());
            visitMethodInsn(INVOKESPECIAL, getInternalName(str), "<init>", "(Ljava/lang/String;)V", false);
        } else {
            getConstant(str);
        }
    }

    private void putReal(TReal real) {
        if (ctx.getName().equals("<clinit>")) {
            visitTypeInsn(NEW, getInternalName(real));
            visitInsn(DUP);
            visitLdcInsn(real.getValue());
            visitMethodInsn(INVOKESPECIAL, getInternalName(real), "<init>", "(D)V", false);
        } else {
            getConstant(real);
        }
    }

    private void putInt(TInt integer) {
        if (ctx.getName().equals("<clinit>")) {
            visitTypeInsn(NEW, getInternalName(integer));
            visitInsn(DUP);
            visitLdcInsn(integer.getValue());
            visitMethodInsn(INVOKESPECIAL, getInternalName(integer), "<init>", "(I)V", false);
        } else {
            getConstant(integer);
        }
    }

    private void putBigInt(TBigInt bigInt) {
        if (ctx.getName().equals("<clinit>")) {
            visitTypeInsn(NEW, getInternalName(bigInt));
            visitInsn(DUP);
            visitLdcInsn(bigInt.toString());
            visitMethodInsn(INVOKESPECIAL, getInternalName(bigInt), "<init>", "(Ljava/lang/String;)V", false);
        } else {
            getConstant(bigInt);
        }
    }

    protected String getInternalName(Object obj) {
        return Type.getType(obj.getClass()).getInternalName();
    }

    protected String getDesc(Object obj) {
        return Type.getType(obj.getClass()).getDescriptor();
    }

    String getInternalName(Class c) {
        return Type.getType(c).getInternalName();
    }

    private String getDesc(Class c) {
        return Type.getType(c).getDescriptor();
    }

    protected String getName(Class c) {
        return Type.getType(c).getClassName().replace(".", "/");
    }

    protected String getDesc(java.lang.reflect.Method m) {
        return Type.getMethodDescriptor(m);
    }

    protected String getDesc(Class<?> c, String methodName, Class... params) {
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ReflectiveMethod that = (ReflectiveMethod) o;
            return paramCount == that.paramCount &&
                    Objects.equals(clazz, that.clazz) &&
                    Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(clazz, name, paramCount);
        }

        private String getName() {
            return name;
        }

        private int getParamCount() {
            return paramCount;
        }
    }
}
