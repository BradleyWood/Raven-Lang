package org.toylang.antlr.ast;

import org.objectweb.asm.Type;

import java.lang.reflect.Method;

public class Interface extends Statement {

    private final QualifiedName name;
    private final String[] names;
    private final Type[] methodTypes;

    public Interface(final QualifiedName name, final String[] names, final Type[] methodTypes) {
        this.name = name;
        this.names = names;
        this.methodTypes = methodTypes;
    }

    public QualifiedName getName() {
        return name;
    }

    public String[] getNames() {
        return names;
    }

    public Type[] getMethodTypes() {
        return methodTypes;
    }

    public static Interface valueOf(Class<?> clazz) {
        if (!clazz.isInterface())
            throw new IllegalArgumentException("Not an interface");
        Method[] methods = clazz.getDeclaredMethods();
        String[] names = new String[methods.length];
        Type[] types = new Type[methods.length];
        for (int i = 0; i < methods.length; i++) {
            names[i] = methods[i].getName();
            types[i] = Type.getType(methods[i]);
        }
        return new Interface(QualifiedName.valueOf(clazz.getName()), names, types);
    }

    @Override
    public String toString() {
        return "Interface[" + name + "]";
    }
}
