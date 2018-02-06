package org.toylang.antlr.ast;

import org.objectweb.asm.Type;

import java.lang.reflect.Method;

public class Interface extends Statement {

    private final QualifiedName name;
    private final Type[] methodTypes;

    public Interface(final QualifiedName name, final Type[] methodTypes) {
        this.name = name;
        this.methodTypes = methodTypes;
    }

    public QualifiedName getName() {
        return name;
    }

    public static Interface valueOf(Class<?> clazz) {
        if (!clazz.isInterface())
            throw new IllegalArgumentException("Not an interface");
        Method[] methods = clazz.getDeclaredMethods();
        Type[] types = new Type[methods.length];
        for (int i = 0; i < methods.length; i++) {
            types[i] = Type.getType(methods[i]);
        }
        return new Interface(QualifiedName.valueOf(clazz.getName()), types);
    }
}
