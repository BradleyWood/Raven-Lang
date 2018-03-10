package org.raven.compiler;

import org.objectweb.asm.*;
import org.raven.antlr.ast.*;
import org.raven.core.wrappers.TObject;
import org.raven.error.Errors;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ClassConstructor extends Method {

    public ClassConstructor(MethodContext ctx, MethodVisitor mv) {
        super(ctx, mv);
    }

    public void visitConstructor(Constructor constructor) {
        scope.beginScope();
        scope.putVar("this");
        if (constructor.getParams() != null) {
            Arrays.stream(constructor.getParams()).forEach(p -> scope.putVar(p.getName().toString()));
        }

        constructor.getInitBlock().getStatements().forEach(stmt -> stmt.accept(this));

        visitVarInsn(ALOAD, 0);

        if (!hasSuperCall(constructor)) {
            int paramCount = 0;

            if (constructor.getSuperParams() != null) {
                if (ctx.getClassDef().hasTlSuper())
                    Arrays.stream(constructor.getSuperParams()).forEach(param -> param.accept(this));
                paramCount = constructor.getSuperParams().length;
            }
            if (paramCount == 0 || ctx.getClassDef().hasTlSuper()) {
                visitMethodInsn(INVOKESPECIAL, ctx.getClassDef().getSuper().toString().replace(".", "/"),
                        "<init>", getDesc(paramCount), false);
            } else {
                invokeJavaSuper(constructor.getSuperParams());
            }
        } else {
            Call superCall = (Call) constructor.getBody().getStatements().get(0);
            constructor.getBody().getStatements().remove(0);
            int paramCount = 0;
            if (superCall.getParams() != null) {
                if (ctx.getClassDef().hasTlSuper())
                    Arrays.stream(superCall.getParams()).forEach(param -> param.accept(this));
                paramCount = superCall.getParams().length;
            }

            if (paramCount == 0 || ctx.getClassDef().hasTlSuper()) {
                visitMethodInsn(INVOKESPECIAL, ctx.getClassDef().getSuper().toString().replace(".", "/"),
                        "<init>", getDesc(superCall.getParams().length), false);
            } else {
                invokeJavaSuper(superCall.getParams());
            }
        }
        if (constructor.getBody() != null) {
            constructor.getBody().accept(this);
        }
        visitInsn(RETURN);
        scope.endScope();
    }

    private void invokeJavaSuper(Expression[] params) {
        try {
            Class clazz = Class.forName(ctx.getClassDef().getSuper().toString());
            LinkedList<Class[]> candidates = new LinkedList<>();

            for (java.lang.reflect.Constructor c : clazz.getConstructors()) {
                if (c.getParameterCount() == params.length) {
                    candidates.add(c.getParameterTypes());
                }
            }

            scope.putVar(" TL_PARAMS ");
            scope.putVar(" SUPER_PARAMS ");

            visitListDef(new ListDef(params));
            visitVarInsn(ASTORE, getLocal(" TL_PARAMS "));

            putSuperCalls(clazz, candidates, params);

        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private void putSuperCalls(Class clazz, LinkedList<Class[]> candidates, Expression[] params) throws NoSuchMethodException {
        Class[] candidate = candidates.getFirst();

        String getParamDesc = "(Lorg/raven/core/wrappers/TObject;[Ljava/lang/Class;)[Ljava/lang/Object;";
        visitVarInsn(ALOAD, getLocal(" TL_PARAMS "));
        visitLdcInsn(params.length);

        visitTypeInsn(ANEWARRAY, getName(Class.class));

        int i = 0;
        for (Class cl : candidate) {
            visitInsn(DUP);
            visitLdcInsn(i++);
            putType(cl);
            visitInsn(AASTORE);
        }

        visitMethodInsn(INVOKESTATIC, getName(TObject.class), "getParams", getParamDesc, false);
        visitVarInsn(ASTORE, getLocal(" SUPER_PARAMS "));

        Label lb = new Label();
        visitVarInsn(ALOAD, getLocal(" SUPER_PARAMS "));
        visitJumpInsn(IFNULL, lb);

        String superDesc = Type.getConstructorDescriptor(clazz.getConstructor(candidate));
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

        Label after = new Label();
        visitJumpInsn(GOTO, after);

        visitLabel(lb);

        candidates.removeFirst();
        if (candidates.size() > 0) {
            putSuperCalls(clazz, candidates, params);
        } else {
            throwIllegalArguments();
        }
        visitLabel(after);
    }

    private void throwIllegalArguments() {
        visitTypeInsn(NEW, IllegalArgumentException.class.getTypeName().replace(".", "/"));
        visitInsn(DUP);
        visitLdcInsn("Illegal parameters in super() call");
        visitMethodInsn(INVOKESPECIAL, getInternalName(IllegalArgumentException.class), "<init>", "(Ljava/lang/String;)V", false);
        visitInsn(ATHROW);
    }

    private void toPrimitive(Class cl) {
        Primitive primitive = Primitive.getPrimitiveType(cl);

        if (primitive != null) {
            primitive.unwrap(this);
        } else {
            Errors.put("Class " + cl + " does not represent a primitive type");
        }
    }

    private void putType(Class c) {
        Primitive type = Primitive.getPrimitiveType(c);
        if (type != null) {
            type.putPrimitiveType(this);
        } else {
            visitLdcInsn(Type.getType(c));
        }
    }

    private String getDesc(int paramCount) {
        StringBuilder builder = new StringBuilder("(");
        for (int i = 0; i < paramCount; i++) {
            builder.append(Type.getType(TObject.class).getDescriptor());
        }
        return builder.append(")V").toString();
    }

    private boolean hasSuperCall(Constructor constructor) {
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
}
