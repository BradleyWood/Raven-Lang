package org.toylang.compiler;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.toylang.antlr.Errors;
import org.toylang.antlr.ast.*;
import org.toylang.core.wrappers.TObject;

import java.util.Arrays;
import java.util.LinkedList;
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

            locals.add(" TL_PARAMS ");
            locals.add(" SUPER_PARAMS ");

            visitListDef(new ListDef(params));
            visitVarInsn(ASTORE, locals.indexOf(" TL_PARAMS "));

            putSuperCalls(clazz, candidates, params);

        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private void putSuperCalls(Class clazz, LinkedList<Class[]> candidates, Expression[] params) throws NoSuchMethodException {
        Class[] candidate = candidates.getFirst();

        String getParamDesc = "(Lorg/toylang/core/wrappers/TObject;[Ljava/lang/Class;)[Ljava/lang/Object;";
        visitVarInsn(ALOAD, locals.indexOf(" TL_PARAMS "));
        visitLdcInsn(params.length);
        visitTypeInsn(ANEWARRAY, getDesc(Class.class));

        int i = 0;
        for (Class cl : candidate) {
            visitInsn(DUP);
            visitLdcInsn(i++);
            putType(cl);
            visitInsn(AASTORE);
        }

        visitMethodInsn(INVOKESTATIC, getDesc(TObject.class), "getParams", getParamDesc, false);
        visitVarInsn(ASTORE, locals.indexOf(" SUPER_PARAMS "));

        Label lb = new Label();
        visitVarInsn(ALOAD, locals.indexOf(" SUPER_PARAMS "));
        visitJumpInsn(IFNULL, lb);

        String superDesc = Type.getConstructorDescriptor(clazz.getConstructor(candidate));
        for (int n = 0; n < params.length; n++) {
            visitVarInsn(ALOAD, locals.indexOf(" SUPER_PARAMS "));
            visitLdcInsn(n);
            visitInsn(AALOAD);
            if (!candidate[n].isPrimitive()) {
                visitTypeInsn(CHECKCAST, candidate[n].getTypeName().replace(".", "/"));
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
        if (cl.equals(int.class)) {
            visitTypeInsn(CHECKCAST, "java/lang/Integer");
            visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
        } else if (cl.equals(double.class)) {
            visitTypeInsn(CHECKCAST, "java/lang/Double");
            visitMethodInsn(INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D", false);
        } else if (cl.equals(float.class)) {
            visitTypeInsn(CHECKCAST, "java/lang/Float");
            visitMethodInsn(INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F", false);
        } else if (cl.equals(byte.class)) {
            visitTypeInsn(CHECKCAST, "java/lang/Byte");
            visitMethodInsn(INVOKEVIRTUAL, "java/lang/Byte", "byteValue", "()B", false);
        } else if (cl.equals(short.class)) {
            visitTypeInsn(CHECKCAST, "java/lang/Short");
            visitMethodInsn(INVOKEVIRTUAL, "java/lang/Short", "shortValue", "()S", false);
        } else if (cl.equals(char.class)) {
            visitTypeInsn(CHECKCAST, "java/lang/Character");
            visitMethodInsn(INVOKEVIRTUAL, "java/lang/Character", "charValue", "()C", false);
        } else if (cl.equals(long.class)) {
            visitTypeInsn(CHECKCAST, "java/lang/Long");
            visitMethodInsn(INVOKEVIRTUAL, "java/lang/Long", "longValue", "()J", false);
        } else if (cl.equals(boolean.class)) {
            visitTypeInsn(CHECKCAST, "java/lang/Boolean");
            visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z", false);
        }
    }

    private void putType(Class c) {
        if (c.equals(int.class)) {
            visitFieldInsn(GETSTATIC, "java/lang/Integer", "TYPE", "Ljava/lang/Class;");
        } else if (c.equals(double.class)) {
            visitFieldInsn(GETSTATIC, "java/lang/Double", "TYPE", "Ljava/lang/Class;");
        } else if (c.equals(float.class)) {
            visitFieldInsn(GETSTATIC, "java/lang/Float", "TYPE", "Ljava/lang/Class;");
        } else if (c.equals(byte.class)) {
            visitFieldInsn(GETSTATIC, "java/lang/Byte", "TYPE", "Ljava/lang/Class;");
        } else if (c.equals(boolean.class)) {
            visitFieldInsn(GETSTATIC, "java/lang/Boolean", "TYPE", "Ljava/lang/Class;");
        } else if (c.equals(char.class)) {
            visitFieldInsn(GETSTATIC, "java/lang/Character", "TYPE", "Ljava/lang/Class;");
        } else if (c.equals(short.class)) {
            visitFieldInsn(GETSTATIC, "java/lang/Short", "TYPE", "Ljava/lang/Class;");
        } else if (c.equals(long.class)) {
            visitFieldInsn(GETSTATIC, "java/lang/Long", "TYPE", "Ljava/lang/Class;");
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
