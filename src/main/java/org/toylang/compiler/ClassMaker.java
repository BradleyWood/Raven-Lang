package org.toylang.compiler;


import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.toylang.antlr.Modifier;
import org.toylang.antlr.ast.*;

import java.util.ArrayList;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;


public class ClassMaker {

    public static final QualifiedName OBJECT = new QualifiedName("java", "lang", "Object");

    private final ClassWriter cw;
    private final ClassDef def;

    private final List<QualifiedName> imports;
    private final List<VarDecl> staticVariables = new ArrayList<>();
    private final List<Fun> staticFunctions = new ArrayList<>();

    public ClassMaker(ClassDef def, List<QualifiedName> imports) {
        this.def = def;
        cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        this.imports = imports;
    }

    /**
     * Creates a skeleton class for classes with no instance methods or fields
     *
     * @param pack The package of the Class
     * @param name The name of the Class
     */
    public ClassMaker(QualifiedName pack, String name, List<QualifiedName> imports) {
        this(new ClassDef(new Modifier[] { Modifier.PUBLIC }, pack, name, OBJECT, new String[0], new ArrayList<>()), imports);
    }
    /**
     * For use in a repl for live interp
     */
    public ClassMaker(List<QualifiedName> imports) {
        this(new ClassDef(new Modifier[] { Modifier.PUBLIC }, "Repl", OBJECT, new String[0], new ArrayList<>()), imports);
    }
    public void make() {
        int modifiers = ACC_SUPER;
        for (Modifier modifier : def.getModifiers()) {
            modifiers += modifier.getModifier();
        }

        cw.visit(V1_7, modifiers, def.getFullName(), def.getSignature(), def.getSuper().toString().replace(".", "/"), def.getInterfaces());
        cw.visitAnnotation(Constants.ANNOTATION_TLFILE_SIG, true).visitEnd();
        cw.visitSource(def.getName().toString()+".tl", null);

        cw.visitField(ACC_PRIVATE + ACC_STATIC + ACC_FINAL, "__CONSTANTS__", "["+Constants.TOYOBJ_SIG, null, null);
        for (VarDecl staticVariable : staticVariables) {
            defineField(staticVariable.getName().toString(), ACC_STATIC + staticVariable.modifiers());
        }

        MethodContext staticContext = new MethodContext(def.getFullName(), null, imports, staticVariables, staticFunctions);
        for(Fun staticFun : staticFunctions) {
            staticContext.setName(staticFun.getName().toString());
            defineMethod(staticContext, staticFun, ACC_STATIC + staticFun.modifiers());
        }

        for (Statement statement : def.getFields()) {
            VarDecl decl = (VarDecl) statement;
            defineField(decl.getName().toString(), decl.modifiers());
        }
        List<Fun> constructors = def.getConstructors();

        if(constructors.size() == 0)
            putDefaultConstructor();

        MethodContext classCtx = new MethodContext(def.getFullName(), "<init>", imports, staticVariables, staticFunctions, def);
        for (Fun constructor : constructors) {
            defineMethod(classCtx, constructor, constructor.modifiers());
        }
        for (Fun fun : def.getMethods()) {
            classCtx.setName(fun.getName().toString());
            defineMethod(classCtx, fun, fun.modifiers());
        }
        Constants.clear();
    }
    private void defineField(String name, int modifiers) {
        cw.visitField(modifiers, name, Constants.TOYOBJ_SIG, null, null);
    }
    private void defineMethod(MethodContext context, Fun fun, int modifiers) {
        String desc = fun.getDesc();

        if(fun.getName().toString().equals("main"))
            modifiers += ACC_PUBLIC;

        Method method = new Method(context, cw.visitMethod(modifiers, fun.getName().toString(), desc, null, fun.getExceptions()));

        if(context.getName().equals("<clinit>")) {
            method.writeConstants();
        }
        method.visitCode();
        fun.accept(method);
        method.end();
    }
    private void putDefaultConstructor() {
        MethodVisitor method = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        method.visitCode();
        method.visitVarInsn(ALOAD, 0);
        method.visitMethodInsn(INVOKESPECIAL, def.getSuper().toString().replace(".", "/"), "<init>", "()V", false);
        method.visitInsn(RETURN);
        method.visitMaxs(0,0);
        method.visitEnd();
    }
    public void addStaticFields(VarDecl decl) {
        staticVariables.add(decl);
    }
    public void addStaticMethods(Fun fun) {
        staticFunctions.add(fun);
    }
    public byte[] getBytes() {
        return cw.toByteArray();
    }
}
