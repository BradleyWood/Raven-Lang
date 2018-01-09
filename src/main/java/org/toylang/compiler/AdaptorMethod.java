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

        scope.beginScope();

        for (VarDecl varDecl : fun.getParams()) {
            scope.putVar(varDecl.getName().toString());
        }

        int i = 0;
        for (VarDecl varDecl : fun.getParams()) {
            visitName(varDecl.getName());
            String var_ = varDecl.getName().toString() + "_";
            c.getParams()[i++] = new QualifiedName(var_);
            visitMethodInsn(INVOKESTATIC, getInternalName(TObject.class), "toToyLang", getDesc(TObject.class, "toToyLang", Object.class), false);

            scope.putVar(var_);
            visitVarInsn(ASTORE, scope.findVar(var_));
        }

        super.visitFunCall(c);

        if (!c.pop()) {
            visitLdcInsn(ret);
            visitMethodInsn(Opcodes.INVOKEVIRTUAL, Constants.TOBJ_NAME, "coerce", "(Ljava/lang/Class;)Ljava/lang/Object;", false);
            visitTypeInsn(CHECKCAST, ret.getInternalName());

            visitInsn(ARETURN);
        } else {
            visitInsn(RETURN);
        }

        scope.endScope();
    }
}
