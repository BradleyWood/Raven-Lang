package org.toylang.compiler;

import org.objectweb.asm.MethodVisitor;
import org.toylang.antlr.ast.Constructor;

import java.util.Arrays;

public class ClassConstructor extends Method {

    public ClassConstructor(MethodContext ctx, MethodVisitor mv) {
        super(ctx, mv);
    }

    public void visitConstructor(Constructor constructor) {
        locals.add("this");
        if (constructor.getParams() != null) {
            Arrays.stream(constructor.getParams()).forEach(p -> locals.add(p.getName().toString()));
        }
        visitVarInsn(ALOAD, 0);
        if (constructor.getSuperParams() == null || constructor.getSuperParams().length == 0) {
            visitMethodInsn(INVOKESPECIAL, ctx.getClassDef().getSuper().toString().replace(".", "/"), "<init>", "()V", false);
        } else {
            Arrays.stream(constructor.getSuperParams()).forEach(param -> param.accept(this));
            visitMethodInsn(INVOKESPECIAL, ctx.getClassDef().getSuper().toString().replace(".", "/"), "<init>", constructor.getSuperConstructorDesc(), false);
        }

        if (constructor.getBody() != null) {
            constructor.getBody().accept(this);
        }
        visitInsn(RETURN);
    }

}
