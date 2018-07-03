package org.raven.compiler;

import org.objectweb.asm.*;
import org.raven.antlr.Modifier;
import org.raven.antlr.Operator;
import org.raven.antlr.ast.*;
import org.raven.core.DefermentStack;
import org.raven.core.Hidden;
import org.raven.core.Intrinsics;
import org.raven.core.wrappers.*;
import org.raven.error.CompilationError;
import org.raven.error.Errors;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.*;
import java.util.stream.Collectors;

public class Method extends MethodVisitor implements TreeVisitor, Opcodes {

    private static final String DEFERMENT_STACK_NAME = " __DEFERMENT__ ";
    private final ArrayList<Integer> lineNumbers = new ArrayList<>();

    private final Stack<Label> continueLabels = new Stack<>();
    private final Stack<Label> breakLabels = new Stack<>();
    private final List<Defer> deferments = new LinkedList<>();

    private boolean hasDeferment = false;
    private final Label defermentLabel = new Label();

    final Scope scope = new Scope();


    protected final MethodContext ctx;
    private boolean disableConstantPool = false;

    Method(final MethodContext ctx, final MethodVisitor mv) {
        super(ASM5, mv);
        this.ctx = ctx;
    }

    int getLocal(final String name) {
        int idx = scope.findVar(name);
        if (idx == -1) {
            Errors.put("Use of variable: " + name + " before it is defined");
            return 0;
        }
        return idx;
    }

    void error(final Statement statement, final String message) {
        Errors.put(new CompilationError(ctx.getClassDef().getSourceTree().getSourceFile(), ctx.getName(), statement, message));
    }

    @Override
    public void visitEnd() {
        visitMaxs(0, 0);
        super.visitEnd();
    }

    @Override
    public void visitIf(final If ifStatement) {
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
    public void visitFor(final For forStatement) {
        Label start = new Label();
        Label conditional = new Label();
        Label after = new Label();
        Label end = new Label();

        continueLabels.push(after);
        breakLabels.push(end);

        scope.beginScope();

        disableConstantPool = true;
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
        disableConstantPool = false;

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
    public void visitWhile(final While whileStatement) {
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
    public void visitReturn(final Return ret) {
        if (ret.getValue() != null)
            visitLine(ret.getValue());

        final boolean isVoid = isVoid();

        if (!isVoid && ret.getValue() == null) {
            putVoid();
        } else if (!isVoid) {
            ret.getValue().accept(this);
        } else if (ret.getValue() != null) {
            error(ret, "Illegal return statement");
        }

        if (hasDeferment) {
            visitJumpInsn(GOTO, defermentLabel);
        } else if (isVoid) {
            visitInsn(RETURN);
        } else {
            visitInsn(ARETURN);
        }
    }

    private boolean isVoid() {
        return Type.getType(ctx.getDesc()).getReturnType().getDescriptor().equals("V");
    }

    @Override
    public void visitConstructor(final Constructor constructor) {
        scope.beginScope();
        scope.putVar("this");

        // add parameters to local scope
        if (constructor.getParams() != null) {
            Arrays.stream(constructor.getParams()).forEach(p -> scope.putVar(p.getName().toString()));
        }

        // initialize any instance fields that aren't initialized manually
        constructor.getInitBlock().getStatements().forEach(stmt -> stmt.accept(this));

        // put this on stack
        visitVarInsn(ALOAD, 0);

        if (!hasSuperCall(constructor)) {
            // no super call is manually provided
            int paramCount = 0;

            if (constructor.getSuperParams() != null) {
                // put the parameters on the stack if the super class is a raven class
                if (ctx.getClassDef().hasTlSuper())
                    Arrays.stream(constructor.getSuperParams()).forEach(param -> param.accept(this));
                paramCount = constructor.getSuperParams().length;
            }
            if (paramCount == 0 || ctx.getClassDef().hasTlSuper()) {
                // either the super constructor is default or the class is a raven class
                visitMethodInsn(INVOKESPECIAL, ctx.getClassDef().getSuper().toString().replace(".", "/"),
                        "<init>", getDesc(paramCount), false);
            } else {
                // super class is a java class and requires a special invocation
                ClassDef definingClass = constructor.getParentByType(ClassDef.class);
                invokeJavaSuper(definingClass.getSuper(), constructor.getSuperParams());
            }
        } else {
            // super() call is provided and is the first statement in the body
            final Call superCall = (Call) constructor.getBody().getStatements().get(0);
            constructor.getBody().getStatements().remove(0);

            int paramCount = 0;

            if (superCall.getParams() != null) {
                // put the parameters on the stack if the super class is a raven class
                if (ctx.getClassDef().hasTlSuper())
                    Arrays.stream(superCall.getParams()).forEach(param -> param.accept(this));
                paramCount = superCall.getParams().length;
            }

            if (paramCount == 0 || ctx.getClassDef().hasTlSuper()) {
                // either the super constructor is default or the class is a raven class
                visitMethodInsn(INVOKESPECIAL, ctx.getClassDef().getSuper().toString().replace(".", "/"),
                        "<init>", getDesc(superCall.getParams().length), false);
            } else {
                // super class is a java class and requires a special invocation
                invokeJavaSuper(superCall, superCall.getParams());
            }
        }

        // define the body of the constructor
        if (constructor.getBody() != null) {
            constructor.getBody().accept(this);
        }

        // constructor has void return
        visitInsn(RETURN);
        scope.endScope();
    }

    /**
     * Invoke a super() call to a java class.This function inserts logic to find the best
     * super constructor given that the parameters are dynamic. If type coercion is not possible
     * the constructor will throw an illegal arguments exception
     *
     * @param params The parameters to pass in the super call
     */
    private void invokeJavaSuper(final Statement call, final Expression[] params) {
        try {
            final Class clazz = Class.forName(ctx.getClassDef().getSuper().toString());
            final LinkedList<Class[]> candidates = new LinkedList<>();

            for (final java.lang.reflect.Constructor c : clazz.getConstructors()) {
                if (c.getParameterCount() == params.length) {
                    candidates.add(c.getParameterTypes());
                }
            }

            scope.putVar(" TL_PARAMS ");
            scope.putVar(" SUPER_PARAMS ");

            visitListDef(new ListDef(params));
            visitVarInsn(ASTORE, getLocal(" TL_PARAMS "));
            // store the parameters as a list

            putSuperCalls(clazz, candidates, params);

        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
            error(call, "Cannot resolve super()");
        }
    }

    /**
     * Place super calls in the constructor. Since the variables are dynamic the super constructor
     * is chosen at runtime based on the how coercible the arguments are
     *
     * @param clazz      The
     * @param candidates The list of parameter types for each class constructor candidate
     * @param params     The parameters to pass
     * @throws NoSuchMethodException
     */
    private void putSuperCalls(final Class clazz, final LinkedList<Class[]> candidates, final Expression[] params) throws NoSuchMethodException {
        final Class[] candidate = candidates.getFirst();

        final String getParamDesc = getDesc(Intrinsics.class, "getParams", TObject.class, Class[].class);

        visitVarInsn(ALOAD, getLocal(" TL_PARAMS "));
        visitLdcInsn(params.length);

        visitTypeInsn(ANEWARRAY, getName(Class.class));

        int i = 0;
        // put the types on the stack that we wish to coerce to
        for (final Class cl : candidate) {
            visitInsn(DUP);
            visitLdcInsn(i++);
            putType(cl);
            visitInsn(AASTORE);
        }

        // perform coercion
        visitMethodInsn(INVOKESTATIC, getName(Intrinsics.class), "getParams", getParamDesc, false);
        visitVarInsn(ASTORE, getLocal(" SUPER_PARAMS "));

        final Label coercionFailedLabel = new Label();
        visitVarInsn(ALOAD, getLocal(" SUPER_PARAMS "));

        // if coercion is impossible for this super constructor is not possible
        // skip the super() call and try the next candidate
        visitJumpInsn(IFNULL, coercionFailedLabel);

        final String superDesc = Type.getConstructorDescriptor(clazz.getConstructor(candidate));
        for (int n = 0; n < params.length; n++) {
            visitVarInsn(ALOAD, getLocal(" SUPER_PARAMS "));
            visitLdcInsn(n);
            visitInsn(AALOAD);
            if (!candidate[n].isPrimitive() || (candidate[n].isArray() && candidate[n].getComponentType().isPrimitive())) {
                visitTypeInsn(CHECKCAST, Type.getType(candidate[n]).getInternalName());
            } else {
                toPrimitive(candidate[n]);
            }
        }

        visitMethodInsn(INVOKESPECIAL, ctx.getClassDef().getSuper().toString().replace(".", "/"),
                "<init>", superDesc, false);

        final Label after = new Label();
        visitJumpInsn(GOTO, after);

        visitLabel(coercionFailedLabel);

        candidates.removeFirst();
        if (candidates.size() > 0) {
            putSuperCalls(clazz, candidates, params);
        } else {
            throwException(IllegalArgumentException.class, "Illegal parameters in super() call");
        }

        // label to jump to the end
        visitLabel(after);
    }

    /**
     * Throw an exception with the given message
     *
     * @param ex      The type of exception to throw
     * @param message The message to leave
     */
    private <T extends Throwable> void throwException(final Class<T> ex, final String message) {
        visitTypeInsn(NEW, ex.getTypeName().replace(".", "/"));
        visitInsn(DUP);
        visitLdcInsn(message);
        visitMethodInsn(INVOKESPECIAL, getInternalName(ex), "<init>", "(Ljava/lang/String;)V", false);
        visitInsn(ATHROW);
    }

    /**
     * Takes a boxed primitive and unwraps it. The primitive must be on the stack already.
     *
     * @param cl The type to unbox
     */
    private void toPrimitive(final Class cl) {
        final Primitive primitive = Primitive.getPrimitiveType(cl);

        if (primitive != null) {
            primitive.unwrap(this);
        } else {
            Errors.put("Class " + cl + " does not represent a primitive type");
        }
    }

    /**
     * Put type on the stack including primitve types
     *
     * @param c The type to put
     */
    private void putType(final Class c) {
        final Primitive type = Primitive.getPrimitiveType(c);
        if (type != null) {
            type.putPrimitiveType(this);
        } else {
            visitLdcInsn(Type.getType(c));
        }
    }

    private void visitLine(final Expression stmt) {
        int line = stmt.getLineNumber();
        if (line >= 0 && !lineNumbers.contains(line)) {
            visitLineNumber(line, new Label());
            lineNumbers.add(line);
        }
    }

    private void visitParams(final VarDecl[] params) {
        if (!ctx.isStatic()) {
            scope.putVar("this");
        } else if (ctx.getName().equals("main")) {
            scope.putVar(" args ");
            if (params.length > 1) {
                Errors.put("Invalid main signature!");
            } else if (params.length == 1) {
                scope.putVar(params[0].getName().toString());
                visitVarInsn(ALOAD, 0);
                visitMethodInsn(INVOKESTATIC, getInternalName(Intrinsics.class), "wrap", getDesc(Intrinsics.class, "wrap", Object.class), false);
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

    private void createDefermentStack() {
        scope.putVar(DEFERMENT_STACK_NAME);
        visitTypeInsn(NEW, getInternalName(DefermentStack.class));
        visitInsn(DUP);
        visitMethodInsn(INVOKESPECIAL, getInternalName(DefermentStack.class), "<init>", "()V", false);
        visitVarInsn(ASTORE, scope.findVar(DEFERMENT_STACK_NAME));
    }

    private void putByte(final byte val) {
        if (val >= 0 && val <= 5) {
            visitInsn(ICONST_0 + val);
        } else {
            visitIntInsn(BIPUSH, val);
        }
    }

    /**
     * Evaluate all deferred expression
     */
    private void execDeferredStatements() {
        final Label end = new Label();

        visitLabel(defermentLabel);

        scope.putVar(" __DEFER_ID__ ");

        int defermentStk = getLocal(DEFERMENT_STACK_NAME);
        int defermentId = getLocal(" __DEFER_ID__ ");

        visitVarInsn(ALOAD, defermentStk);

        visitMethodInsn(INVOKEVIRTUAL, getInternalName(DefermentStack.class), "nextDeferment", "()I", false);
        visitInsn(DUP);
        visitVarInsn(ISTORE, defermentId);
        visitJumpInsn(IFLT, end);

        for (final Defer deferment : deferments) {
            int id = (byte) deferments.indexOf(deferment);

            if (id != (byte) id) {
                Errors.put("Too many deferment statements in " + ctx.getOwner() + "." + ctx.getName() + "()");
            }

            visitVarInsn(ILOAD, defermentId);
            putByte((byte) id);

            Label next = new Label();
            visitJumpInsn(IF_ICMPNE, next);

            // INVOKE FUNCTION
            deferment.getCall().accept(this);

            visitLabel(next);

        }
        visitJumpInsn(GOTO, defermentLabel);

        visitLabel(end);
    }

    @Override
    public void visitFun(final Fun fun) {
        scope.beginScope();
        visitParams(fun.getParams());

        if (fun.getName().toString().equals("<clinit>")) {
            writeConstants();
        }

        if (fun.getName().toString().equals("main") && fun.getParams().length < 2) {
            visitMethodInsn(INVOKESTATIC, getInternalName(Intrinsics.class), "useSanitizedExceptionHandler", "()V", false);
        }

        hasDeferment = fun.getBody().hasChildOfType(Defer.class);

        if (hasDeferment) {
            createDefermentStack();
        }

        fun.getBody().accept(this);

        Statement stmt = null;
        int idx = fun.getBody().getStatements().size() - 1;
        if (idx != -1)
            stmt = fun.getBody().getStatements().get(idx);
        if (!(stmt instanceof Return)) { // last stmt isnt return so add one
            visitReturn(new Return(null));
        }

        if (hasDeferment) {
            execDeferredStatements();
        }

        if (isVoid()) {
            visitInsn(RETURN);
        } else {
            visitInsn(ARETURN);
        }

        scope.endScope();
    }

    private String getFunDescriptor(final Expression[] params) {
        StringBuilder sig = new StringBuilder("(");
        for (Expression ignored : params) {
            sig.append(Constants.TOBJ_SIG);
        }
        sig.append(")" + Constants.TOBJ_SIG);
        return sig.toString();
    }

    int counter = 0;

    @Override
    public void visitGo(final Go go) {
        Call goFun = go.getGoFun();

        String lambdaName = "lambda$" + ctx.getName() + "$" + counter++;
        String desc = getFunDescriptor(goFun.getParams());
        StringBuilder paramsDesc = new StringBuilder();
        for (int i = 0; i < goFun.getParams().length; i++) {
            paramsDesc.append(getDesc(TObject.class));
        }
        visitTypeInsn(NEW, "java/lang/Thread");
        visitInsn(DUP);
        Arrays.stream(goFun.getParams()).forEach(a -> a.accept(this));
        visitInvokeDynamicInsn("run", "(" + paramsDesc + ")Ljava/lang/Runnable;", LAMBDA_BOOTSTRAP,
                Type.getType("()V"), new Handle(Opcodes.H_INVOKESTATIC, ctx.getOwner(), lambdaName, desc),
                Type.getType("()V"));
        visitMethodInsn(INVOKESPECIAL, "java/lang/Thread", "<init>", "(Ljava/lang/Runnable;)V", false);

        if (!go.pop()) {
            visitInsn(DUP);
        }

        visitMethodInsn(INVOKEVIRTUAL, "java/lang/Thread", "start", "()V", false);

        if (!go.pop()) {
            visitMethodInsn(INVOKESTATIC, getInternalName(Intrinsics.class), "wrap", getDesc(Intrinsics.class, "wrap", Object.class), false);
        }

        VarDecl[] params = new VarDecl[goFun.getParams().length];
        for (int i = 0; i < params.length; i++) {
            params[i] = new VarDecl(new QualifiedName(String.valueOf(i)), new Literal(TNull.NULL));
        }
        Fun lambda = new Fun(new QualifiedName(lambdaName), new Block(goFun),
                new Modifier[]{Modifier.PRIVATE, Modifier.STATIC, Modifier.SYNTHETIC}, new String[0], params);
        if (ctx.isStatic()) {
            lambda.addModifier(Modifier.STATIC);
        }
        ctx.addSynthetic(lambda);
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
    public void visitFunCall(final Call call) {
        visitLine(call);

        String desc = getFunDescriptor(call.getParams());

        if (call.getName().toString().equals("super")) {
            Errors.put(ctx.getClassDef().getSourceTree().getSourceFile() + " line " + call.getLineNumber() + ": Super() not allowed here");
            return;
        } else if (call.getName().toString().equals("this")) {
            Errors.put(ctx.getClassDef().getSourceTree().getSourceFile() + " line " + call.getLineNumber() + ": This() not allowed here");
            return;
        }

        if (call.getPrecedingExpr() != null) {
            final Expression precedingExpr = call.getPrecedingExpr();
            if (precedingExpr instanceof QualifiedName) {
                String owner = getInternalNameFromImports(precedingExpr.toString());
                if (owner != null) {
                    invokeStaticFun(owner, call.getName().toString(), desc, call.getParams());
                } else {
                    precedingExpr.accept(this);
                    java.lang.reflect.Method method = findRavenFunction(call.getName().toString(), call.getParams().length);
                    if (method != null) {
                        invokeVirtualFun(call.getName().toString(), call.getParams(), method);
                    } else {
                        invokeVirtualFun(call.getName().toString(), call.getParams());
                    }
                }
            } else {
                precedingExpr.accept(this);
                java.lang.reflect.Method method = findRavenFunction(call.getName().toString(), call.getParams().length);
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
                    invokeStaticExact(funOwner, call.getName().toString(), desc, call.getParams());
                } else {
                    if (ctx.isStatic()) {
                        invokeStaticExact(funOwner, call.getName().toString(), desc, call.getParams());
                    } else {
                        Fun fun = SymbolMap.resolveFun(ctx.getOwner(), ctx.getOwner(), call.getName().toString(), call.getParams().length);
                        if (fun != null && (fun.modifiers() & ACC_STATIC) == 0 && fun.isJavaMethod()) {
                            visitVarInsn(ALOAD, getLocal("this"));
                            visitMethodInsn(INVOKESTATIC, getName(Intrinsics.class), "wrap",
                                    getDesc(Intrinsics.class, "wrap", Object.class), false);
                            invokeVirtualFun(fun.getName().toString(), call.getParams());
                        } else if (fun != null && (fun.modifiers() & ACC_STATIC) == 0) {
                            visitVarInsn(ALOAD, getLocal("this"));
                            Arrays.stream(call.getParams()).forEach(param -> param.accept(this));
                            visitMethodInsn(INVOKEVIRTUAL, funOwner, call.getName().toString(), desc, false);
                        } else if (fun != null) {
                            Arrays.stream(call.getParams()).forEach(param -> param.accept(this));

                            String funDesc = fun.isJavaMethod() ? fun.getDesc() : desc;
                            visitMethodInsn(INVOKESTATIC, funOwner, call.getName().toString(), funDesc, false);

                            if (fun.isJavaMethod()) {
                                Type methodType = Type.getMethodType(funDesc);
                                Primitive p = Primitive.getPrimitiveType(methodType.getReturnType().getDescriptor());
                                if (p != null) {
                                    p.wrap(this);
                                }
                                visitMethodInsn(INVOKESTATIC, getName(Intrinsics.class), "wrap",
                                        getDesc(Intrinsics.class, "wrap", Object.class), false);
                            }
                        } else {
                            error(call, "Cannot resolve method");
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

    /**
     * Searches for builtin virtual functions that apply to all types.
     *
     * @param name       The name of the method
     * @param paramCount The number of parameters
     * @return The reflective method handle if found, otherwise null
     */
    private java.lang.reflect.Method findRavenFunction(final String name, final int paramCount) {
        for (java.lang.reflect.Method m : TObject.class.getDeclaredMethods()) {
            if (m.getAnnotation(Hidden.class) != null || m.getParameterCount() != paramCount)
                continue;
            if (m.getName().equals(name))
                return m;
        }
        return null;
    }

    /**
     * Used to check if a method is overloaded or non-existent
     *
     * @param funOwner   The owner of the method
     * @param name       The name of the method
     * @param paramCount The number of parameters
     * @return the number of methods with the name and given param count
     */
    private int methodCount(final String funOwner, final String name, final int paramCount) {
        int count = 0;
        try {
            Class<?> klazz = Class.forName(funOwner.replace("/", "."));
            for (java.lang.reflect.Method method : klazz.getDeclaredMethods()) {
                if (method.getName().equals(name) && method.getParameterCount() == paramCount) {
                    count++;
                }
            }
        } catch (ClassNotFoundException ignored) {
        }
        return count;
    }

    /**
     * Invoke a static function with the invoke_dynamic instruction. Used when a method
     * is overloaded with the same number of parameters.
     *
     * @param funOwner The owner of the method
     * @param funName  The name of the method
     * @param params   The parameters
     */
    private void dynamicInvokeStatic(final String funOwner, final String funName, final Expression[] params) {
        Type owner = Type.getType("L" + (funOwner.replace(".", "/")) + ";");

        visitListDef(new ListDef(params));
        visitTypeInsn(CHECKCAST, "org/raven/core/wrappers/TList");
        visitInvokeDynamicInsn(funName, "(Lorg/raven/core/wrappers/TList;)Lorg/raven/core/wrappers/TObject;",
                INVOKE_STATIC_BOOTSTRAP,
                owner, params.length);
    }

    /**
     * Assumes value is on stack
     *
     * @param type
     */
    private void coerce(final Type type) {
        Primitive p = Primitive.getPrimitiveType(type.getDescriptor());
        if (p == null) {
            visitLdcInsn(type);
        } else {
            p.putPrimitiveType(this);
        }
        visitMethodInsn(INVOKEVIRTUAL, Constants.TOBJ_NAME, "coerce", getDesc(TObject.class, "coerce", Class.class), false);
        if (p != null) {
            p.unwrap(this);
        } else {
            visitTypeInsn(CHECKCAST, type.getInternalName());
        }
    }

    /**
     * Invoke a static function
     *
     * @param funOwner The class that owns this the function
     * @param funName  The name of the function
     * @param desc     The method descriptor
     * @param params   The method parameters
     */
    private void invokeStaticFun(final String funOwner, final String funName, final String desc, final Expression[] params) {
        Fun f = SymbolMap.resolveFun(ctx.getOwner(), funOwner, funName, params.length);
        // if the function can be resolved, it is a tl function
        int count = methodCount(funOwner, funName, params.length);

        if (f != null && !f.isJavaMethod()) {
            invokeStaticExact(funOwner, funName, desc, params);
        } else if (f == null || count != 1) {
            dynamicInvokeStatic(funOwner, funName, params);
        } else {
            Type methodType = Type.getMethodType(f.getDesc());
            Type[] types = Type.getArgumentTypes(f.getDesc());
            for (int i = 0; i < params.length; i++) {
                params[i].accept(this);
                coerce(types[i]);
            }
            visitMethodInsn(INVOKESTATIC, funOwner, funName, f.getDesc(), false);

            Primitive p = Primitive.getPrimitiveType(methodType.getReturnType().getDescriptor());

            if (!methodType.getReturnType().equals(Type.VOID_TYPE)) {
                if (p != null) {
                    p.wrap(this);
                }
                visitMethodInsn(INVOKESTATIC, getName(Intrinsics.class), "wrap",
                        getDesc(Intrinsics.class, "wrap", Object.class), false);
            } else {
                // method returns void so push null onto stack
                putNull();
            }
        }
    }

    /**
     * Invokes the static function and performs no type coercions.
     * Used when the method is a Raven-Lang function
     *
     * @param owner  The class containing the method
     * @param name   The name of the method
     * @param desc   The method descriptor
     * @param params The parameters
     */
    private void invokeStaticExact(final String owner, final String name, final String desc, final Expression[] params) {
        Fun f = SymbolMap.resolveFun(ctx.getOwner(), owner, name, params.length);
        if (f == null && !owner.equals(Constants.BUILTIN_NAME)) {
            Errors.put(ctx.getOwner() + " Cannot resolve function: " + owner + "." + name);
        }
        for (Expression param : params) {
            param.accept(this);
        }
        visitMethodInsn(INVOKESTATIC, owner, name, desc, false);
    }

    /**
     * Invokes a virtual function. The receiver object must already be on the stack
     *
     * @param name   The name of the method
     * @param params The method parameters
     */
    private void invokeVirtualFun(final String name, final Expression[] params) {
        visitListDef(new ListDef(params));
        visitTypeInsn(CHECKCAST, getInternalName(TList.class));
        mv.visitInvokeDynamicInsn(name, "(Lorg/raven/core/wrappers/TObject;Lorg/raven/core/wrappers/TList;)Lorg/raven/core/wrappers/TObject;",
                INVOKE_VIRTUAL_BOOTSTRAP, params.length);
    }

    /**
     * Invoke a virtual function when the type and method is known.
     *
     * @param name   The name of the method
     * @param params The parameters
     * @param method The method handle
     */
    private void invokeVirtualFun(final String name, final Expression[] params, final java.lang.reflect.Method method) {
        String desc = Type.getMethodDescriptor(method);
        Arrays.stream(params).forEach(expression -> expression.accept(this));
        visitMethodInsn(INVOKEVIRTUAL, Constants.TOBJ_NAME, name, desc, false);

        Primitive p = Primitive.getUnboxedPrimitive(method.getReturnType());
        if (p != null) {
            p.wrap(this);
        }

        if (!method.getReturnType().isAssignableFrom(TObject.class)) {
            visitMethodInsn(INVOKESTATIC, getName(Intrinsics.class), "wrap", getDesc(Intrinsics.class, "wrap", Object.class), false);
        }
    }

    /**
     * Instantiate a new object
     *
     * @param owner  The class type
     * @param params The constructor parameters
     */
    private void newObject(final String owner, final Expression[] params) {
        Type type = Type.getType("L" + owner + ";");

        visitListDef(new ListDef(params));
        visitTypeInsn(CHECKCAST, getName(TList.class));

        String name = owner.substring(1 + owner.lastIndexOf("/"));

        mv.visitInvokeDynamicInsn("new" + name, "(Lorg/raven/core/wrappers/TList;)Lorg/raven/core/wrappers/TObject;",
                CONSTRUCTOR_BOOTSTRAP, type, params.length);
    }

    @Override
    public void visitName(final QualifiedName name) {
        accessField(name, true);
        if (name.pop())
            visitInsn(POP);
    }

    private void accessField(final QualifiedName name, final boolean load) {
        visitLine(name);

        String[] names = name.getNames();
        int localIdx = scope.findVar(names[0]);

        if (names[0].equals("this") || names[0].equals("super")) {
            if (ctx.isStatic()) {
                Errors.put("\"" + names[0] + "\" not allowed in non-static context");
                return;
            } else if (load) {
                visitVarInsn(ALOAD, getLocal("this"));
            } else if (names.length == 1) {
                Errors.put("Cannot reassign \"" + names[0] + "\"");
                return;
            }
        }

        // some form of action on a local var
        if (localIdx != -1) {
            if (names.length == 1) {
                visitVarInsn(load ? ALOAD : ASTORE, localIdx);
            } else {
                accessField(new QualifiedName(names[0]), true);
                String[] virtualNames = Arrays.copyOfRange(names, 1, names.length);
                accessVirtualField(virtualNames, load);
            }
        } else {
            if (names.length == 1) {
                // local variable or field in the same class
                accessLocalField(names[0], load);
            } else {
                accessNonLocalField(names, load);
            }
        }
    }

    /**
     * Access a locally defined field or variable
     *
     * @param name The field name
     * @param load whether we are reading or writing to the field
     */
    private void accessLocalField(final String name, final boolean load) {
        final VarDecl decl = SymbolMap.resolveField(ctx.getOwner(), ctx.getOwner(), name);
        final VarDecl var = ctx.getClassDef().findVar(name);

        if (ctx.isStatic()) {
            // static field access in the same class
            if (decl != null && decl.hasModifier(Modifier.STATIC)) {
                accessStaticField(ctx.getOwner(), decl.getName().toString(), load);
            } else if (decl != null) {
                Errors.put("Use of non-static variable " + name + " in a static context");
            } else {
                Errors.put("Variable " + name + " has not been defined.");
            }
        } else if ((decl != null && decl.hasModifier(Modifier.STATIC))) {
            accessStaticField(ctx.getOwner(), decl.getName().toString(), load);
        } else if (var != null && var.hasModifier(Modifier.STATIC)) {
            accessStaticField(var.getTypeDesc(), var.getName().toString(), load);
        } else {
            if (var != null) {
                accessVirtualField(decl, load);
            } else if (decl != null) {
                visitVarInsn(ALOAD, getLocal("this"));
                visitMethodInsn(INVOKESTATIC, getInternalName(Intrinsics.class), "wrap", getDesc(Intrinsics.class, "wrap", Object.class), false);
                visitTypeInsn(CHECKCAST, getInternalName(TObject.class));
                accessVirtualField(name, load);
            } else {
                Errors.put("Variable " + name + " has not been defined.");
            }
        }
    }

    /**
     * Access a non-locally defined field. This may include static fields
     * in other classes or fields nested inside of local variables or fields
     *
     * @param names Names of the fields (fields may have several levels of nesting)
     * @param load  whether we are reading or writing to the field
     */
    private void accessNonLocalField(final String[] names, final boolean load) {
        String importedClass = getInternalNameFromImports(names[0]);
        VarDecl decl = SymbolMap.resolveField(ctx.getOwner(), ctx.getOwner(), names[0]);
        if (importedClass != null) {
            accessStaticField(importedClass, names[1], load || names.length > 2);
            String[] virtualNames = Arrays.copyOfRange(names, 2, names.length);
            accessVirtualField(virtualNames, load);
        } else {
            if (decl != null) {
                visitFieldInsn(GETSTATIC, ctx.getOwner().replace(".", "/"), names[0], Constants.TOBJ_SIG);
                String[] virtualNames = Arrays.copyOfRange(names, 1, names.length);
                accessVirtualField(virtualNames, load);
            } else {
                Errors.put("Variable " + names[0] + " has not been defined.");
            }
        }
    }

    /**
     * Read or write to a static field in the specified class
     *
     * @param owner The class in which the field is defined
     * @param name  The name of the field
     * @param load  whether we are reading or writing to the field
     */
    private void accessStaticField(final String owner, final String name, final boolean load) {
        VarDecl decl = SymbolMap.resolveField(ctx.getOwner(), owner, name);

        if (decl == null) {
            Errors.put("Cannot resolve field " + owner + "." + name);
            return;
        }

        if (decl.isJavaField()) {
            if (load) {
                String type = decl.getTypeDesc();
                final Primitive p = Primitive.getPrimitiveType(type);
                visitFieldInsn(GETSTATIC, owner, name, type);
                if (p != null) {
                    p.wrap(this);
                }
                visitMethodInsn(INVOKESTATIC, getName(Intrinsics.class), "wrap", getDesc(Intrinsics.class, "wrap", Object.class), false);
                return;
            }
            coerce(Type.getType(decl.getTypeDesc()));
        }
        visitFieldInsn(load ? GETSTATIC : PUTSTATIC, owner, name, decl.getTypeDesc());
    }

    /**
     * Read from or store to a virtual field in the current class.
     * Must be in a non-static context.
     *
     * @param var  The var definition
     * @param load whether we are reading or writing to the field
     */
    private void accessVirtualField(final VarDecl var, final boolean load) {
        int idx = getLocal("this");
        if (idx != 0) {
            Errors.put(ctx.getOwner() + " line " + var.getName().getLineNumber() + " Cannot access field in non static context: " + var.getName());
            return;
        }
        visitVarInsn(ALOAD, idx);
        if (!load) {
            visitInsn(SWAP);
        }
        visitFieldInsn(load ? GETFIELD : PUTFIELD, ctx.getOwner(), var.getName().toString(), getDesc(TObject.class));
    }

    /**
     * Read from or store to a virtual field. The instance
     * reference must be already on the stack
     *
     * @param names The names of the fields
     * @param load  whether we are reading or writing to the field
     */
    private void accessVirtualField(final String[] names, final boolean load) {
        if (names.length == 0) {
            return;
        }
        for (int i = 0; i < names.length - 1; i++) {
            accessVirtualField(names[i], true);
        }
        if (!load) {
            visitInsn(SWAP);
        }
        accessVirtualField(names[names.length - 1], load);
    }

    /**
     * Load or store to a virtual field
     * Instance must already be on stack as well as the new value if we are writing to the field.
     *
     * @param name The field name
     * @param load whether we are reading or writing to the field
     */
    private void accessVirtualField(final String name, final boolean load) {
        if (load) {
            mv.visitInvokeDynamicInsn("get" + name, "(Lorg/raven/core/wrappers/TObject;)Lorg/raven/core/wrappers/TObject;",
                    GET_BOOTSTRAP);
        } else {
            mv.visitInvokeDynamicInsn("set" + name, "(Lorg/raven/core/wrappers/TObject;Lorg/raven/core/wrappers/TObject;)Lorg/raven/core/wrappers/TObject;",
                    SET_BOOTSTRAP);
        }
    }

    /**
     * Initialize a new array with the specified values
     *
     * @param expressions The expressions to fill the array with
     */
    private void createArray(final Expression[] expressions) {
        visitLdcInsn(expressions.length);
        visitTypeInsn(ANEWARRAY, Constants.TOBJ_NAME);
        for (int i = 0; i < expressions.length; i++) {
            visitInsn(DUP);
            visitLdcInsn(i);
            expressions[i].accept(this);
            visitInsn(AASTORE);
        }
    }

    @Override
    public void visitBlock(final Block block) {
        scope.beginScope();
        block.getStatements().forEach(stmt -> stmt.accept(this));
        scope.endScope();
    }

    @Override
    public void visitExpressionGroup(final ExpressionGroup group) {
        group.accept(this);
        if (group.pop())
            visitInsn(POP);
    }

    @Override
    public void visitVarDecl(final VarDecl decl) {
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
    public void visitImport(final Import importStatement) {

    }

    @Override
    public void visitBinOp(final BinOp op) {
        if (op.getOp() != Operator.ASSIGNMENT && op.getLeft() != null)
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
            case INC:
            case DEC:
            case NOT:
                visitMethodInsn(INVOKEVIRTUAL, Constants.TOBJ_NAME, op.getOp().name, getDesc(TObject.class, op.getOp().name), false);
                break;
            default:
                visitMethodInsn(INVOKEVIRTUAL, Constants.TOBJ_NAME, op.getOp().name, getDesc(TObject.class, op.getOp().name, TObject.class), false);
                break;
        }
        if (op.pop() && op.getOp() != Operator.ASSIGNMENT)
            visitInsn(POP);
    }

    @Override
    public void visitLiteral(final Literal literal) {
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
        if (literal.pop())
            visitInsn(POP);
    }

    @Override
    public void visitListDef(final ListDef def) {
        visitTypeInsn(NEW, getInternalName(TList.class));
        visitInsn(DUP);

        String typeDesc = Type.getMethodDescriptor(Type.VOID_TYPE, Type.getType(TObject[].class));
        createArray(def.getExpressions());

        visitMethodInsn(INVOKESPECIAL, getInternalName(TList.class), "<init>", typeDesc, false);

        if (def.pop())
            visitInsn(POP);
    }

    @Override
    public void visitListIdx(final ListIndex idx) {
        idx.getPrecedingExpr().accept(this);
        for (Expression expression : idx.getIndex()) {
            expression.accept(this);
            visitMethodInsn(INVOKEVIRTUAL, Constants.TOBJ_NAME, "get", getDesc(TObject.class, "get", TObject.class), false);
        }
        if (idx.pop())
            visitInsn(POP);
    }

    @Override
    public void visitClassDef(final ClassDef def) {
        // inner class
    }

    @Override
    public void visitDictDef(final DictDef def) {
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
    public void visitAnnotation(final Annotation annotation) {
        final Class clazz = getClass(annotation.getName());
        if (clazz == null) {
            Errors.put("Cannot resolve annotation: " + annotation.getName());
            return;
        }

        if (!clazz.isAnnotation()) {
            Errors.put("line " + annotation.getLineNumber() + ": type " + clazz.getTypeName() + " is not an" +
                    "annotation.");
            return;
        }

        final Map<String, Boolean> requirements = getAnnotationRequirements(clazz);

        final List<String> keys = Arrays.stream(annotation.getKeys()).map(QualifiedName::toString)
                .collect(Collectors.toList());
        final List<Literal> values = Arrays.asList(annotation.getValues());

        final boolean valid = requirements.entrySet().stream().filter(Map.Entry::getValue)
                .allMatch((e) -> keys.contains(e.getKey()));

        if (!valid) {
            Errors.put("line " + annotation.getLineNumber() + ": missing non-default annotation parameters");
            return;
        }

        final boolean visible = isRuntimeRetention(clazz);
        final AnnotationVisitor av = visitAnnotation(Type.getType(clazz).getDescriptor(), visible);
        for (int i = 0; i < keys.size(); i++) {
            final String name = keys.get(i);
            final Boolean required = requirements.get(name);

            if (required != null) {
                for (java.lang.reflect.Method m : clazz.getMethods()) {
                    if (m.getName().equals(name)) {
                        try {
                            Object value = values.get(i).getValue().coerce(m.getReturnType());
                            av.visit(name, value);
                        } catch (final UnsupportedOperationException e) {
                            Errors.put("line " + annotation.getLineNumber() + ": invalid annotation " +
                                    "parameter value name=" + name + " value=" + values.get(i) + " expected type=" +
                                    m.getReturnType());
                        }
                    }
                }
            } else {
                Errors.put("line " + annotation.getLineNumber() + ": Invalid annotation parameter: " +
                        keys.get(i));
            }
        }
        av.visitEnd();
    }

    private boolean isRuntimeRetention(Class<? extends Annotation> annotation) {
        Retention retentionPolicy = annotation.getDeclaredAnnotation(Retention.class);

        if (retentionPolicy != null) {
            return retentionPolicy.value() == RetentionPolicy.RUNTIME;
        }

        return false;
    }

    private Map<String, Boolean> getAnnotationRequirements(final Class clazz) {
        final Map<String, Boolean> requirements = new HashMap<>();

        for (final java.lang.reflect.Method method : clazz.getDeclaredMethods()) {
            requirements.put(method.getName(), method.getDefaultValue() == null);
        }

        return requirements;
    }

    private Class getClass(final String name) {
        try {
            for (final QualifiedName imp : ctx.getImports()) {
                final String lastName = imp.getNames()[imp.getNames().length - 1];
                if (lastName.equals(name)) {
                    return Class.forName(imp.toString());
                }
            }
            return Class.forName(name);
        } catch (final ClassNotFoundException ignored) {
        }
        return null;
    }

    @Override
    public void visitAnnotationDef(final AnnoDef def) {

    }

    @Override
    public void visitTryCatchFinally(final TryCatchFinally tcf) {
        boolean hasFinally = tcf.getFinallyBlock() != null;

        // empty try; can optimize away
        if (tcf.getBody().getStatements().isEmpty()) {
            if (hasFinally && !tcf.getFinallyBlock().getStatements().isEmpty()) {
                tcf.getFinallyBlock().accept(this);
            }
            return;
        }

        Label bodyStart = new Label();
        Label bodyEnd = new Label();
        Label catchBlock = new Label();
        Label after = new Label();

        Label f1 = new Label();
        Label f2 = new Label();

        visitLabel(bodyStart);
        tcf.getBody().accept(this);
        visitLabel(bodyEnd);

        if (hasFinally) {
            tcf.getFinallyBlock().accept(this);
        }

        visitJumpInsn(GOTO, after);

        visitLabel(catchBlock);

        scope.putVar(tcf.getExceptionName().toString());
        visitMethodInsn(INVOKESTATIC, getInternalName(Intrinsics.class), "wrap", getDesc(Intrinsics.class, "wrap", Object.class), false);
        visitVarInsn(ASTORE, getLocal(tcf.getExceptionName().toString()));

        tcf.getHandler().accept(this);

        if (hasFinally) {
            visitLabel(f2);
            tcf.getFinallyBlock().accept(this);
            visitJumpInsn(GOTO, after);
            visitLabel(f1);

            scope.putVar(" __ex__ ");
            visitVarInsn(ASTORE, getLocal(" __ex__ "));

            tcf.getFinallyBlock().accept(this);

            visitVarInsn(ALOAD, getLocal(" __ex__ "));
            visitInsn(ATHROW);
        }

        visitLabel(after);

        visitTryCatchBlock(bodyStart, bodyEnd, catchBlock, "java/lang/Throwable");

        if (hasFinally) {
            visitTryCatchBlock(bodyStart, bodyEnd, f1, null);
            visitTryCatchBlock(catchBlock, f2, f1, null);
        }
    }

    @Override
    public void visitRaise(final Raise raise) {
        raise.getExpression().accept(this);
        visitMethodInsn(INVOKEVIRTUAL, getName(TObject.class), "toObject", getDesc(TObject.class, "toObject"), false);
        visitInsn(DUP);
        visitTypeInsn(INSTANCEOF, "java/lang/Throwable");

        Label invalidException = new Label();
        visitJumpInsn(IFEQ, invalidException);

        visitTypeInsn(CHECKCAST, "java/lang/Throwable");
        visitInsn(ATHROW);

        visitLabel(invalidException);
        visitInsn(POP);
        visitTypeInsn(NEW, "java/lang/RuntimeException");
        visitInsn(DUP);
        visitLdcInsn("Cannot throw non-exception type");
        visitMethodInsn(INVOKESPECIAL, "java/lang/RuntimeException", "<init>", "(Ljava/lang/String;)V", false);
        visitInsn(ATHROW);
    }

    @Override
    public void visitDefer(final Defer defer) {
        if (!deferments.contains(defer)) {
            deferments.add(defer);
        }

        visitVarInsn(ALOAD, getLocal(DEFERMENT_STACK_NAME));
        visitIntInsn(BIPUSH, deferments.indexOf(defer));
        visitMethodInsn(INVOKEVIRTUAL, getInternalName(DefermentStack.class), "defer",
                getDesc(DefermentStack.class, "defer", int.class), false);


        final Call call = defer.getCall();
        final Expression[] expressions = call.getParams();
        final Expression precedingExpr = call.getPrecedingExpr();

        boolean objOnStack = false;

        for (int i = expressions.length - 1; i >= 0; i--) {
            visitVarInsn(ALOAD, getLocal(DEFERMENT_STACK_NAME));
            expressions[i].accept(this);

            visitMethodInsn(INVOKEVIRTUAL, getInternalName(DefermentStack.class), "push",
                    getDesc(DefermentStack.class, "push", TObject.class), false);
            // inject facade to maintain compatibility
            expressions[i] = popExpression();
        }

        if (precedingExpr instanceof QualifiedName && getInternalNameFromImports(precedingExpr.toString()) == null
                || precedingExpr != null) {
            visitVarInsn(ALOAD, getLocal(DEFERMENT_STACK_NAME));
            precedingExpr.accept(this);

            visitMethodInsn(INVOKEVIRTUAL, getInternalName(DefermentStack.class), "push",
                    getDesc(DefermentStack.class, "push", TObject.class), false);

            call.setPrecedingExpr(popExpression());
        }

        call.setPop(true);
    }

    /**
     * Represents a facade expression. Pulls pre-evaluated value from the deferment stack
     */
    private Expression popExpression() {
        return new Expression() {
            @Override
            public void accept(final TreeVisitor visitor) {
                visitVarInsn(ALOAD, getLocal(DEFERMENT_STACK_NAME));
                visitMethodInsn(INVOKEVIRTUAL, getInternalName(DefermentStack.class), "pop",
                        getDesc(DefermentStack.class, "pop"), false);
            }
        };
    }

    private boolean hasSuperCall(final Constructor constructor) {
        Block block = constructor.getBody();
        if (block != null) {
            List<Statement> lst = block.getStatements();
            if (lst != null && lst.size() > 0) {
                if (lst.get(0) instanceof Call) {
                    Call c = (Call) lst.get(0);
                    if (c.getName().toString().equals("super")) {
                        if (c.getPrecedingExpr() == null) {
                            return true;
                        } else {
                            Errors.put("Invalid super call: " + c);
                        }
                    }
                }
            }
        }
        return false;
    }

    private void assignListIdx(final ListIndex idx) {
        idx.getPrecedingExpr().accept(this);
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

    private void writeConstants() {
        visitLdcInsn(ctx.getConstants().size());
        visitTypeInsn(ANEWARRAY, Constants.TOBJ_NAME);
        int i = 0;
        for (TObject obj : ctx.getConstants()) {
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
            } else if (obj instanceof TVoid) {
                putVoid();
            } else {
                Errors.put("Unidentified Constant type");
                visitInsn(ACONST_NULL);
            }
            visitInsn(AASTORE);
            i++;
        }
        visitFieldInsn(PUTSTATIC, ctx.getOwner(), "__CONSTANTS__", getDesc(TObject[].class));
    }

    private void getConstant(final TObject obj) {
        int idx = ctx.getConstants().indexOf(obj);
        visitFieldInsn(GETSTATIC, ctx.getOwner(), "__CONSTANTS__", getDesc(TObject[].class));

        if (idx >= 0) {
            visitLdcInsn(idx);
        } else {
            visitLdcInsn(ctx.getConstants().size());
            ctx.getConstants().add(obj);
        }
        visitInsn(AALOAD);
    }

    private void putNull() {
        visitFieldInsn(GETSTATIC, getInternalName(TNull.NULL), "NULL", getDesc(TNull.NULL));
    }

    private void putVoid() {
        visitFieldInsn(GETSTATIC, getInternalName(TVoid.VOID), "VOID", getDesc(TVoid.VOID));
    }

    private void putBoolean(final TBoolean bool) {
        visitFieldInsn(GETSTATIC, getInternalName(bool), bool.isTrue() ? "TRUE" : "FALSE", getDesc(bool));
    }

    private void putString(final TString str) {
        if (ctx.getName().equals("<clinit>") || disableConstantPool) {
            visitTypeInsn(NEW, getInternalName(str));
            visitInsn(DUP);
            visitLdcInsn(str.toString());
            visitMethodInsn(INVOKESPECIAL, getInternalName(str), "<init>", "(Ljava/lang/String;)V", false);
        } else {
            getConstant(str);
        }
    }

    private void putReal(final TReal real) {
        if (ctx.getName().equals("<clinit>") || disableConstantPool) {
            visitTypeInsn(NEW, getInternalName(real));
            visitInsn(DUP);
            visitLdcInsn(real.getValue());
            visitMethodInsn(INVOKESPECIAL, getInternalName(real), "<init>", "(D)V", false);
        } else {
            getConstant(real);
        }
    }

    private void putInt(final TInt integer) {
        if (ctx.getName().equals("<clinit>") || disableConstantPool) {
            visitTypeInsn(NEW, getInternalName(integer));
            visitInsn(DUP);
            visitLdcInsn(integer.getValue());
            visitMethodInsn(INVOKESPECIAL, getInternalName(integer), "<init>", "(I)V", false);
        } else {
            getConstant(integer);
        }
    }

    private void putBigInt(final TBigInt bigInt) {
        if (ctx.getName().equals("<clinit>") || disableConstantPool) {
            visitTypeInsn(NEW, getInternalName(bigInt));
            visitInsn(DUP);
            visitLdcInsn(bigInt.toString());
            visitMethodInsn(INVOKESPECIAL, getInternalName(bigInt), "<init>", "(Ljava/lang/String;)V", false);
        } else {
            getConstant(bigInt);
        }
    }

    protected String getInternalName(final Object obj) {
        return Type.getType(obj.getClass()).getInternalName();
    }

    protected String getDesc(final int paramCount) {
        final StringBuilder builder = new StringBuilder("(");
        for (int i = 0; i < paramCount; i++) {
            builder.append(Type.getType(TObject.class).getDescriptor());
        }
        return builder.append(")V").toString();
    }

    protected String getDesc(final Object obj) {
        return Type.getType(obj.getClass()).getDescriptor();
    }

    String getInternalName(final Class c) {
        return Type.getType(c).getInternalName();
    }

    private String getDesc(final Class c) {
        return Type.getType(c).getDescriptor();
    }

    protected String getName(final Class c) {
        return Type.getType(c).getClassName().replace(".", "/");
    }

    protected String getDesc(final java.lang.reflect.Method m) {
        return Type.getMethodDescriptor(m);
    }

    protected String getDesc(final Class<?> c, final String methodName, final Class... params) {
        try {
            return Type.getMethodDescriptor(c.getMethod(methodName, params));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Errors.put("Cannot find builtin method: " + c.getName() + "." + methodName);
        }
        return null;
    }

    private String getInternalNameFromImports(final String name) {
        for (QualifiedName qualifiedName : ctx.getImports()) {
            String lastName = qualifiedName.getNames()[qualifiedName.getNames().length - 1];
            if (lastName.equals(name)) {
                return qualifiedName.toString().replace(".", "/");
            }
        }
        return null;
    }

    private static final Handle GET_BOOTSTRAP = new Handle(H_INVOKESTATIC,
            "org/raven/core/Intrinsics", "bootstrapGetter",
            "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;",
            false);

    private static final Handle SET_BOOTSTRAP = new Handle(H_INVOKESTATIC,
            "org/raven/core/Intrinsics", "bootstrapSetter",
            "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;",
            false);

    private static final Handle CONSTRUCTOR_BOOTSTRAP = new Handle(H_INVOKESTATIC,
            "org/raven/core/Intrinsics", "bootstrapConstructor",
            "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/Class;I)Ljava/lang/invoke/CallSite;", false);

    private static final Handle LAMBDA_BOOTSTRAP = new Handle(Opcodes.H_INVOKESTATIC,
            "java/lang/invoke/LambdaMetafactory", "metafactory",
            "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;", false);

    private static final Handle INVOKE_STATIC_BOOTSTRAP = new Handle(H_INVOKESTATIC,
            "org/raven/core/Intrinsics", "bootstrap",
            "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/Class;I)Ljava/lang/invoke/CallSite;", false);

    private static final Handle INVOKE_VIRTUAL_BOOTSTRAP = new Handle(H_INVOKESTATIC,
            "org/raven/core/Intrinsics", "bootstrapVirtual",
            "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;I)Ljava/lang/invoke/CallSite;", false);
}
