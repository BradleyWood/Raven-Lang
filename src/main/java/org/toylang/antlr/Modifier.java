package org.toylang.antlr;


import static org.objectweb.asm.Opcodes.*;

public enum Modifier {

    PUBLIC(ACC_PUBLIC),
    PRIVATE(ACC_PRIVATE),
    OPEN(-ACC_FINAL),
    STATIC(ACC_STATIC),
    ABSTRACT(ACC_ABSTRACT),
    SYNTHETIC(ACC_SYNTHETIC);
    int modifier;

    Modifier(int modifier) {
        this.modifier = modifier;
    }

    public int getModifier() {
        return modifier;
    }

    public static Modifier getModifier(String name) {
        switch (name) {
            case "public":
                return PUBLIC;
            case "private":
                return PRIVATE;
            case "open":
                return OPEN;
        }
        return null;
    }
}
