package org.toylang.compiler;

import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public enum Primitive {

    BYTE("byte", "B", "java/lang/Byte", ILOAD, IRETURN, byte.class, Byte.class),
    SHORT("short", "S", "java/lang/Short", ILOAD, IRETURN, short.class, Short.class),
    INT("int", "I", "java/lang/Integer", ILOAD, IRETURN, int.class, Integer.class),
    LONG("long", "J", "java/lang/Long", LLOAD, LRETURN, long.class, Long.class),
    FLOAT("float", "F", "java/lang/Float", FLOAD, FRETURN, float.class, Float.class),
    DOUBLE("double", "D", "java/lang/Double", DLOAD, DRETURN, double.class, Double.class),
    CHAR("char", "C", "java/lang/Character", ILOAD, IRETURN, char.class, Character.class),
    BOOLEAN("boolean", "Z", "java/lang/Boolean", ILOAD, IRETURN, boolean.class, Boolean.class),
    VOID("void", "V", "java/lang/Void", RETURN, 0, void.class, Void.class);

    private final String name;
    private final String desc;
    private final String wrapper;
    private final int loadInstruction;
    private final int retInstruction;
    private final Class<?> primitiveClass;
    private final Class<?> boxedClass;

    Primitive(final String name, final String desc, final String wrapper,
              final int loadInstruction, final int retInstruction, final Class<?> primitiveClass, final Class<?> boxedClass) {
        this.name = name;
        this.desc = desc;
        this.wrapper = wrapper;
        this.primitiveClass = primitiveClass;
        this.boxedClass = boxedClass;
        this.loadInstruction = loadInstruction;
        this.retInstruction = retInstruction;
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
     *
     * @param mv
     */
    public void wrap(MethodVisitor mv) {
        mv.visitMethodInsn(INVOKESTATIC, wrapper, "valueOf", "(" + desc + ")" + getInternalName(), false);
    }

    /**
     * Gets the type for this primitive and puts in on the stack
     *
     * @param mv
     */
    public void putPrimitiveType(MethodVisitor mv) {
        mv.visitFieldInsn(GETSTATIC, wrapper, "TYPE", "Ljava/lang/Class;");
    }

    /**
     * Return from the method with the correct instruction
     *
     * @param mv
     */
    public void ret(MethodVisitor mv) {
        mv.visitInsn(retInstruction);
    }

    /**
     * Put the primitive at local idx onto the stack
     *
     * @param mv
     * @param idx
     */
    public void load(MethodVisitor mv, int idx) {
        mv.visitVarInsn(loadInstruction, idx);
    }

    /**
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
        Primitive unboxed = getUnboxedPrimitive(clazz);
        if (unboxed != null) {
            return unboxed;
        }
        return getBoxedPrimitive(clazz);
    }

    public static Primitive getUnboxedPrimitive(Class<?> clazz) {
        for (Primitive primitive : Primitive.values()) {
            if (primitive.primitiveClass.equals(clazz))
                return primitive;
        }
        return null;
    }

    public static Primitive getBoxedPrimitive(Class<?> clazz) {
        for (Primitive primitive : Primitive.values()) {
            if (primitive.boxedClass.equals(clazz))
                return primitive;
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
