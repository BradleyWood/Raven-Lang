package org.toylang.compiler;

import org.objectweb.asm.MethodVisitor;
import org.toylang.antlr.Errors;
import org.toylang.antlr.ast.Block;
import org.toylang.antlr.ast.Call;
import org.toylang.antlr.ast.Constructor;
import org.toylang.antlr.ast.Statement;

import java.util.Arrays;
import java.util.List;

public class ClassConstructor extends Method {

    public ClassConstructor(MethodContext ctx, MethodVisitor mv) {
        super(ctx, mv);
    }

    public void visitConstructor(Constructor constructor) {
        locals.add("this");
        if (constructor.getParams() != null) {
            Arrays.stream(constructor.getParams()).forEach(p -> locals.add(p.getName().toString()));
        }

        constructor.getInitBlock().getStatements().forEach(stmt -> stmt.accept(this));

        visitVarInsn(ALOAD, 0);

        if (!hasSuperCall(constructor)) {
            if (constructor.getSuperParams() == null || constructor.getSuperParams().length == 0) {
                visitMethodInsn(INVOKESPECIAL, ctx.getClassDef().getSuper().toString().replace(".", "/"), "<init>", "()V", false);
            } else {
                Arrays.stream(constructor.getSuperParams()).forEach(param -> param.accept(this));
                visitMethodInsn(INVOKESPECIAL, ctx.getClassDef().getSuper().toString().replace(".", "/"), "<init>", constructor.getSuperConstructorDesc(), false);
            }
        } else {
            Call superCall = (Call) constructor.getBody().getStatements().get(0);
            constructor.getBody().getStatements().remove(0);
            Arrays.stream(superCall.getParams()).forEach(param -> param.accept(this));
            visitMethodInsn(INVOKESPECIAL, ctx.getClassDef().getSuper().toString().replace(".", "/"), "<init>", constructor.getSuperConstructorDesc(), false);
        }
        if (constructor.getBody() != null) {
            constructor.getBody().accept(this);
        }
        visitInsn(RETURN);
    }

    private boolean hasSuperCall(Constructor constructor) {
        Block block = constructor.getBody();
        if (block != null) {
            List<Statement> lst = block.getStatements();
            if (lst != null && lst.size() > 0) {
                if (lst.get(0) instanceof Call) {
                    Call c = (Call) lst.get(0);
                    if (c.getName().toString().equals("super")) {
                        if(c.getPrecedingExpr() == null)
                            return true;
                        else
                            Errors.put("Invalid super call: "+c);
                    }
                }
            }
        }
        return false;
    }
}
