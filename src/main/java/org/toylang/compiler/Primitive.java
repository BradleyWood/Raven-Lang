package org.toylang.compiler;

import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public enum Primitive {

    BYTE("byte", "B", "java/lang/Byte", byte.class, Byte.class),
    SHORT("float", "S", "java/lang/Short", short.class, Short.class),
    INT("int", "I", "java/lang/Integer", int.class, Integer.class),
    LONG("long", "J", "java/lang/Long", long.class, Long.class),
    FLOAT("float", "F", "java/lang/Float", float.class, Float.class),
    DOUBLE("double", "D", "java/lang/Double", double.class, Double.class),
    CHAR("char", "C", "java/lang/Character", char.class, Character.class),
    BOOLEAN("boolean", "Z", "java/lang/Boolean", boolean.class, Boolean.class),
    VOID("void", "V", "java/lang/Void", void.class, Void.class);

    private String name;
    private String desc;
    private String wrapper;
    private Class<?>[] classes;

    Primitive(final String name, final String desc, final String wrapper, Class<?>... classes) {
        this.name = name;
        this.desc = desc;
        this.wrapper = wrapper;
        this.classes = classes;
    }

    /**
     * Assumes that the wrapped type is already on the stack
     *
     * @param mv
     */
    public void unwrap(MethodVisitor mv) {
        mv.visitTypeInsn(CHECKCAST, wrapper);
        mv.visitMethodInsn(INVOKEVIRTUAL, wrapper, name + "Value", "()" + desc, false);
    }

    /**
     * Wrap the primitive
     * @param mv
     */
    public void wrap(MethodVisitor mv) {
        mv.visitMethodInsn(INVOKESTATIC, wrapper, "valueOf", "(" + desc + ")" + getInternalName(), false);
    }

    /**
     * Gets the type for this primitive and puts in on the stack
     * @param mv
     */
    public void putPrimitiveType(MethodVisitor mv) {
        mv.visitFieldInsn(GETSTATIC, wrapper, "TYPE", "Ljava/lang/Class;");
    }

    /**
     *
     * @return The internal name for the wrapped type
     */
    public String getInternalName() {
        return "L" + wrapper + ";";
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getWrapper() {
        return wrapper;
    }

    public static Primitive getPrimitiveType(Class<?> clazz) {
        for (Primitive primitive : Primitive.values()) {
            for (Class<?> aClass : primitive.classes) {
                if (aClass.equals(clazz))
                    return primitive;
            }
        }
        return null;
    }

    public static Primitive getPrimitiveType(String desc) {
        for (Primitive primitive : Primitive.values()) {
            if (primitive.getDesc().equals(desc))
                return primitive;
        }
        return null;
    }

    public static boolean isPrimitive(String desc) {
        return getPrimitiveType(desc) != null;
    }
}
