package org.raven.antlr;


import static org.objectweb.asm.Opcodes.*;

public enum Modifier {

    PUBLIC(ACC_PUBLIC),
    PRIVATE(ACC_PRIVATE),
    OPEN(-ACC_FINAL),
    STATIC(ACC_STATIC),
    ABSTRACT(ACC_ABSTRACT),
    INTERFACE(ACC_INTERFACE),
    SYNTHETIC(ACC_SYNTHETIC);
    int modifier;

    Modifier(final int modifier) {
        this.modifier = modifier;
    }

    public int getModifier() {
        return modifier;
    }

    public static Modifier getModifier(final String name) {
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
