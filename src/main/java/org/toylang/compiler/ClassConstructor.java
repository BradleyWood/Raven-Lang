package org.toylang.compiler;

import org.objectweb.asm.MethodVisitor;
import org.toylang.antlr.ast.Constructor;

public class ClassConstructor extends Method {

    public ClassConstructor(MethodContext ctx, MethodVisitor mv) {
        super(ctx, mv);
    }

    public void visitConstructor(Constructor constructor) {

    }
}
