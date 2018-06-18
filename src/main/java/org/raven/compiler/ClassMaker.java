package org.raven.compiler;


import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.raven.antlr.Modifier;
import org.raven.antlr.ast.*;
import org.raven.error.Errors;

import java.util.Arrays;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;


public class ClassMaker {

    public static final QualifiedName OBJECT = new QualifiedName("java", "lang", "Object");

    private final ClassWriter cw;
    private final ClassDef def;

    private final List<QualifiedName> imports;

    public ClassMaker(final ClassDef def, final List<QualifiedName> imports) {
        this.def = def;
        cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        this.imports = imports;
        this.imports.addAll(Arrays.asList(Constants.COMMON_IMPORTS));
    }

    public void make() {
        int modifiers = ACC_SUPER;
        for (Modifier modifier : def.getModifiers()) {
            modifiers += modifier.getModifier();
        }
        String[] interfaces = new String[def.getInterfaces().length];
        for (int i = 0; i < def.getInterfaces().length; i++) {
            interfaces[i] = def.getInterfaces()[i].toString().replace(".", "/");
        }

        cw.visit(V1_7, modifiers, def.getFullName(), null, def.getSuper().toString().replace(
                ".", "/"), interfaces);
        cw.visitAnnotation(Constants.ANNOTATION_TLFILE_SIG, true).visitEnd();
        cw.visitSource(def.getSourceTree().getSourceFile(), null);

        cw.visitField(ACC_PRIVATE + ACC_STATIC + ACC_SYNTHETIC + ACC_FINAL, "__CONSTANTS__",
                "[" + Constants.TOBJ_SIG, null, null);
        for (VarDecl staticVariable : def.getFields()) {
            if (!staticVariable.isPrivate()) {
                staticVariable.setPublic();
            }
            defineField(staticVariable.getName().toString(), staticVariable.modifiers());
        }

        List<Constructor> constructors = def.getConstructors();

        MethodContext classCtx = new MethodContext(def.getFullName(), "<init>", imports, def);
        for (Constructor constructor : constructors) {
            if (!constructor.isPrivate()) {
                constructor.setPublic();
            }
            defineConstructor(classCtx, constructor);
        }
        for (Fun fun : def.getMethods()) {
            if (!fun.isPrivate()) {
                fun.setPublic();
            }
            classCtx.setName(fun.getName().toString());
            classCtx.setStatic(fun.hasModifier(Modifier.STATIC));
            defineMethod(classCtx, fun, fun.modifiers());

            Fun override = SymbolMap.resolveJavaFun(def.getSuper().toString(), fun.getName().toString(),
                    fun.getParams().length);
            if (override != null) {
                Fun delegate = createDelegate(Type.getType(override.getDesc()), override.getName().toString());
                defineMethod(classCtx, delegate, fun.getParams().length);
            }
        }

        for (Fun fun : classCtx.getSyntheticFunctions()) {
            classCtx.setName(fun.getName().toString());
            classCtx.setStatic(fun.hasModifier(Modifier.STATIC));
            defineMethod(classCtx, fun, fun.modifiers());
        }

        for (QualifiedName iFace : def.getInterfaces()) {
            Interface in = SymbolMap.resolveInterface(iFace.toString().replace(".", "/"));
            if (in == null) {
                Errors.put("Unresolved interface: " + iFace);
                continue;
            }
            for (int i = 0; i < in.getMethodTypes().length; i++) {
                String name = in.getNames()[i];
                String desc = in.getMethodTypes()[i].getDescriptor();

                if (!def.containsMethod(name, in.getMethodTypes()[i].getArgumentTypes().length)) {
                    System.err.println("Warning: unimplemented interface method: " + name + " " + desc);
                    createStub(in.getNames()[i], in.getMethodTypes()[i]);
                } else if (!def.containsExact(name, desc)) {
                    classCtx.setStatic(false);
                    classCtx.setName(in.getNames()[i]);
                    Fun delegate = createDelegate(in.getMethodTypes()[i], in.getNames()[i]);
                    defineMethod(classCtx, delegate, delegate.modifiers());
                }
            }
        }
    }

    /**
     * Creates an empty stub method that does nothing, used as
     * a placeholder for unimplemented interface methods
     *
     * @param name The name of the method
     * @param type The method type
     */
    private void createStub(final String name, final Type type) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, name, type.getDescriptor(), null, new String[0]);
        mv.visitCode();

        Primitive p = Primitive.getPrimitiveType(type.getReturnType().getDescriptor());

        if (p != null) {
            switch (p) {
                case INT:
                case BYTE:
                case SHORT:
                case BOOLEAN:
                case CHAR:
                    mv.visitInsn(ICONST_0);
                    break;
                case LONG:
                    mv.visitInsn(LCONST_0);
                    break;
                case FLOAT:
                    mv.visitInsn(FCONST_0);
                    break;
                case DOUBLE:
                    mv.visitInsn(FCONST_0);
                    break;
            }
            p.ret(mv);
        } else {
            mv.visitInsn(ACONST_NULL);
            mv.visitInsn(ARETURN);
        }

        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    private Fun createDelegate(final Type t, final String methodName) {
        Block body = new Block();
        VarDecl[] params = new VarDecl[t.getArgumentTypes().length];
        for (int i = 0; i < params.length; i++) {
            params[i] = new VarDecl(new QualifiedName("__" + i + "__"), null);
        }
        Fun fun = new Fun(new QualifiedName(methodName), body, new Modifier[]{Modifier.PUBLIC}, new String[0], params);
        fun.forceDescriptor(t.getDescriptor());

        Expression[] jParams = new Expression[fun.getParams().length];
        for (int i = 0; i < jParams.length; i++) {
            jParams[i] = new QualifiedName(fun.getParams()[i].getName().toString());
        }
        Call call = new Call(fun.getName(), jParams);

        if (t.getReturnType().equals(Type.VOID_TYPE)) {
            call.setPop(true);
        }

        body.addBefore(call);

        return fun;
    }

    private void defineField(final String name, final int modifiers) {
        cw.visitField(modifiers, name, Constants.TOBJ_SIG, null, null);
    }

    private void defineConstructor(final MethodContext ctx, final Constructor constructor) {
        int modifiers = 0;
        for (Modifier modifier : constructor.getModifiers()) {
            modifiers += modifier.getModifier();
        }

        Method method = new Method(ctx, cw.visitMethod(modifiers, "<init>", constructor.getDesc(), null,
                null));
        constructor.getAnnotations().forEach(method::visitAnnotation);

        method.visitCode();
        constructor.accept(method);
        method.visitEnd();
    }

    private void defineMethod(final MethodContext context, final Fun fun, final int modifiers) {
        String desc = fun.getDesc();

        Method method;

        if (fun.isJavaMethod()) {
            method = new AdaptorMethod(context, cw.visitMethod(modifiers, fun.getName().toString(), desc, null,
                    fun.getExceptions()));
        } else {
            method = new Method(context, cw.visitMethod(modifiers, fun.getName().toString(), desc, null,
                    fun.getExceptions()));
        }

        fun.getAnnotations().forEach(annotation -> annotation.accept(method));

        method.visitCode();
        fun.accept(method);
        method.visitEnd();
    }

    public byte[] getBytes() {
        return cw.toByteArray();
    }
}
