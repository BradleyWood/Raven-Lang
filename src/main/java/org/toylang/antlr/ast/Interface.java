package org.toylang.antlr.ast;

import org.objectweb.asm.Type;
import org.toylang.antlr.Modifier;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Interface extends ClassDef {

    public static final QualifiedName IFACE_PARENT = new QualifiedName("java", "lang", "Object");
    private final String[] names;
    private final Type[] methodTypes;

    public Interface(final QualifiedName pkg, final String name, final String[] names, final Type[] methodTypes,
                     QualifiedName[] interfaces) {
        super(new Modifier[]{Modifier.PUBLIC, Modifier.ABSTRACT, Modifier.INTERFACE}, pkg, name, IFACE_PARENT,
                interfaces, new LinkedList<>());
        this.names = names;
        this.methodTypes = methodTypes;
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
        List<String> names = new LinkedList<>();
        List<Type> types = new LinkedList<>();
        for (int i = 0; i < methods.length; i++) {
            names.add(methods[i].getName());
            types.add(Type.getType(methods[i]));
        }
        Class<?>[] p_interfaces = clazz.getInterfaces();
        QualifiedName[] interfaceQualifiedNames = new QualifiedName[clazz.getInterfaces().length];
        for (int i = 0; i < interfaceQualifiedNames.length; i++) {
            interfaceQualifiedNames[i] = QualifiedName.valueOf(p_interfaces[i].getName());
            Interface pi = valueOf(p_interfaces[i]);
            names.addAll(Arrays.asList(pi.getNames()));
            types.addAll(Arrays.asList(pi.getMethodTypes()));
        }
        for (String name : names) {
            System.err.println(clazz.getName()+":: "+ name);
        }
        return new Interface(QualifiedName.valueOf(clazz.getPackage().toString()), clazz.getSimpleName(),
                names.toArray(new String[names.size()]), types.toArray(new Type[types.size()]), interfaceQualifiedNames);
    }

    @Override
    public String toString() {
        return "Interface[" + getFullName() + "]";
    }
}
