package org.toylang.compiler;

import org.objectweb.asm.Type;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.toylang.antlr.ast.*;
import org.toylang.core.wrappers.TObject;

/**
 *
 */
public class AdaptorMethod extends Method {

    protected AdaptorMethod(MethodContext ctx, MethodVisitor mv) {
        super(ctx, mv);
    }

    @Override
    public void visitFun(Fun fun) {
        Call c = (Call) fun.getBody().getStatements().get(0);

        Type ret = Type.getReturnType(fun.getDesc());
        Type[] paramTypes = Type.getArgumentTypes(fun.getDesc());

        scope.beginScope();

        for (VarDecl varDecl : fun.getParams()) {
            scope.putVar(varDecl.getName().toString());
        }

        int i = 0;
        for (VarDecl varDecl : fun.getParams()) {
            Primitive p = Primitive.getPrimitiveType(paramTypes[i].getDescriptor());

            String var_ = varDecl.getName().toString() + "_";
            c.getParams()[i++] = new QualifiedName(var_);

            if (p != null) {
                int idx = scope.findVar(varDecl.getName().toString());
                if (idx < 0)
                    Errors.put("Could not find local var with name: " + varDecl.getName().toString());
                p.load(this, idx);
                p.wrap(this);
            } else {
                visitName(varDecl.getName());
            }
            visitMethodInsn(INVOKESTATIC, getInternalName(TObject.class), "toToyLang", getDesc(TObject.class, "toToyLang", Object.class), false);

            scope.putVar(var_);
            visitVarInsn(ASTORE, scope.findVar(var_));
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

    private boolean isPrimitive(Type type) {
        return Primitive.isPrimitive(type.getDescriptor());
    }
}
