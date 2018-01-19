package org.toylang.compiler;


import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.toylang.antlr.Modifier;
import org.toylang.antlr.ast.*;
import org.toylang.core.wrappers.TNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.objectweb.asm.Opcodes.*;


public class ClassMaker {

    public static final QualifiedName OBJECT = new QualifiedName("java", "lang", "Object");

    private final ClassWriter cw;
    private final ClassDef def;

    private final List<QualifiedName> imports;

    public ClassMaker(ClassDef def, List<QualifiedName> imports) {
        this.def = def;
        cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        this.imports = imports;
        this.imports.addAll(Arrays.asList(Constants.COMMON_IMPORTS));
    }

    /**
     * Creates a skeleton class for classes with no instance methods or fields
     *
     * @param pack The package of the Class
     * @param name The name of the Class
     */
    public ClassMaker(QualifiedName pack, String name, List<QualifiedName> imports) {
        this(new ClassDef(new Modifier[]{Modifier.PUBLIC}, pack, name, OBJECT, new QualifiedName[0], new ArrayList<>()), imports);
    }

    /**
     * For use in a repl for live interp
     */
    public ClassMaker(List<QualifiedName> imports) {
        this(new ClassDef(new Modifier[]{Modifier.PUBLIC}, new QualifiedName("repl"), "Repl", OBJECT, new QualifiedName[0], new ArrayList<>()), imports);
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

        cw.visit(V1_7, modifiers, def.getFullName(), def.getSignature(), def.getSuper().toString().replace(".", "/"), interfaces);
        cw.visitAnnotation(Constants.ANNOTATION_TLFILE_SIG, true).visitEnd();
        cw.visitSource(def.getSourceTree().getSourceFile(), null);

        cw.visitField(ACC_PRIVATE + ACC_STATIC + ACC_FINAL, "__CONSTANTS__", "[" + Constants.TOBJ_SIG, null, null);
        for (VarDecl staticVariable : def.getFields()) {
            defineField(staticVariable.getName().toString(), staticVariable.modifiers());
        }

        List<Constructor> constructors = def.getConstructors();

        if (constructors.size() == 0)
            putDefaultConstructor();

        MethodContext classCtx = new MethodContext(def.getFullName(), "<init>", imports, def);
        for (Constructor constructor : constructors) {
            defineConstructor(classCtx, constructor);
        }
        for (Fun fun : def.getMethods()) {
            classCtx.setName(fun.getName().toString());
            classCtx.setStatic(fun.hasModifier(Modifier.STATIC));
            defineMethod(classCtx, fun, fun.modifiers());
        }
        Constants.clear();
    }

    private void defineField(String name, int modifiers) {
        cw.visitField(modifiers, name, Constants.TOBJ_SIG, null, null);
    }

    private void defineConstructor(MethodContext ctx, Constructor constructor) {
        int modifiers = 0;
        for (Modifier modifier : constructor.getModifiers()) {
            modifiers += modifier.getModifier();
        }
        ClassConstructor cc = new ClassConstructor(ctx, cw.visitMethod(modifiers, "<init>", constructor.getDesc(), null, null));
        cc.visitCode();
        cc.visitConstructor(constructor);
        cc.visitEnd();
    }

    private void defineMethod(MethodContext context, Fun fun, int modifiers) {
        String desc = fun.getDesc();

        if (fun.getName().toString().equals("main"))
            modifiers += ACC_PUBLIC;

        Method method;

        if (fun.isJavaMethod()) {
            method = new AdaptorMethod(context, cw.visitMethod(modifiers, fun.getName().toString(), desc, null, fun.getExceptions()));
        } else {
            method = new Method(context, cw.visitMethod(modifiers, fun.getName().toString(), desc, null, fun.getExceptions()));
        }

        fun.getAnnotations().forEach(annotation -> annotation.accept(method));

        method.visitCode();
        fun.accept(method);
        method.visitEnd();

        // generate methods for lambda expressions
        List<Go> goStatements = fun.getBody().getStatements().stream().filter(stmt -> stmt instanceof Go).map(stmt -> (Go) stmt).collect(Collectors.toList());
        String methodName = context.getName();
        int counter = 0;
        for (Go goStatement : goStatements) {
            String lambdaName = "lambda$" + methodName + "$" + counter++;
            VarDecl[] params = new VarDecl[goStatement.getGoFun().getParams().length];
            for (int i = 0; i < params.length; i++) {
                params[i] = new VarDecl(new QualifiedName(String.valueOf(i)), new Literal(TNull.NULL));
            }
            Fun lamba = new Fun(new QualifiedName(lambdaName), new Block(goStatement.getGoFun()), new Modifier[]{}, new String[0], params);
            context.setName(lambdaName);
            defineMethod(context, lamba, ACC_STATIC + ACC_PRIVATE + ACC_SYNTHETIC);
        }
    }

    private void putDefaultConstructor() {
        MethodVisitor method = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        method.visitCode();
        method.visitVarInsn(ALOAD, 0);
        method.visitMethodInsn(INVOKESPECIAL, def.getSuper().toString().replace(".", "/"), "<init>", "()V", false);
        method.visitInsn(RETURN);
        method.visitMaxs(0, 0);
        method.visitEnd();
    }

    public byte[] getBytes() {
        return cw.toByteArray();
    }
}
