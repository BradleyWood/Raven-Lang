package org.raven.compiler;

import org.objectweb.asm.Type;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.raven.antlr.ast.*;
import org.raven.core.Intrinsics;
import org.raven.error.Errors;

/**
 *
 */
public class AdaptorMethod extends Method {

    protected AdaptorMethod(final MethodContext ctx, final MethodVisitor mv) {
        super(ctx, mv);
    }

    @Override
    public void visitFun(final Fun fun) {
        Call c = (Call) fun.getBody().getStatements().get(0);

        Type ret = Type.getReturnType(fun.getDesc());
        Type[] paramTypes = Type.getArgumentTypes(fun.getDesc());
        Primitive[] primitives = new Primitive[paramTypes.length];

        scope.beginScope();

        if (!ctx.isStatic()) {
            scope.putVar("this");
        }

        int i = 0;
        for (VarDecl varDecl : fun.getParams()) {
            scope.putVar(varDecl.getName().toString());
            primitives[i] = Primitive.getPrimitiveType(paramTypes[i].getDescriptor());
            if (primitives[i] == Primitive.DOUBLE || primitives[i] == Primitive.LONG) {
                scope.putVar(varDecl.getName().toString() + "_2");
                // need extra 4 bytes for long or double
            }
            i++;
        }

        i = 0;
        for (VarDecl varDecl : fun.getParams()) {
            String var_ = varDecl.getName().toString() + "_";
            c.getParams()[i] = new QualifiedName(var_);

            if (primitives[i] != null) {
                int idx = scope.findVar(varDecl.getName().toString());
                if (idx < 0)
                    Errors.put("Could not find local var with name: " + varDecl.getName().toString());
                primitives[i].load(this, idx);
                primitives[i].wrap(this);
            } else {
                visitName(varDecl.getName());
            }
            visitMethodInsn(INVOKESTATIC, getInternalName(Intrinsics.class), "wrap", getDesc(Intrinsics.class, "wrap", Object.class), false);

            scope.putVar(var_);
            visitVarInsn(ASTORE, scope.findVar(var_));
            i++;
        }

        super.visitFunCall(c);

        if (!c.pop()) {
            Primitive primitive = Primitive.getPrimitiveType(ret.getDescriptor());

            if (primitive != null) {
                primitive.putPrimitiveType(mv);
            } else {
                visitLdcInsn(ret);
            }

            visitMethodInsn(Opcodes.INVOKEVIRTUAL, Constants.TOBJ_NAME, "coerce", "(Ljava/lang/Class;)Ljava/lang/Object;", false);

            if (primitive != null) {
                primitive.unwrap(mv);
                primitive.ret(this);
            } else {
                visitTypeInsn(CHECKCAST, ret.getInternalName());
                visitInsn(ARETURN);
            }
        } else {
            visitInsn(RETURN);
        }

        scope.endScope();
    }

    private boolean isPrimitive(final Type type) {
        return Primitive.isPrimitive(type.getDescriptor());
    }
}
