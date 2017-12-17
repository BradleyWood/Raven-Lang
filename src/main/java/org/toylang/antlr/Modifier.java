package org.toylang.antlr;


import static org.objectweb.asm.Opcodes.*;

public enum Modifier {

    PUBLIC(ACC_PUBLIC),
    PRIVATE(ACC_PRIVATE),
    OPEN(-ACC_FINAL),
    SYNTHETIC(ACC_SYNTHETIC);
    int modifier;

    Modifier(int modifier) {
        this.modifier = modifier;
    }

    public int getModifier() {
        return modifier;
    }

    public static Modifier getModifier(String name) {
        if (name.equals("public")) {
            return PUBLIC;
        } else if (name.equals("private")) {
            return PRIVATE;
        } else if (name.equals("open")) {
            return OPEN;
        }
        return null;
    }
}
