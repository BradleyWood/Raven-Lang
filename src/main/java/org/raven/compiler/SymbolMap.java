package org.raven.compiler;

import org.raven.antlr.Modifier;
import org.raven.antlr.ast.*;
import org.raven.core.wrappers.TObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

public class SymbolMap {

    private static HashMap<String, ClassDef> CLASS_MAP = new HashMap<>();
    private static HashMap<String, Interface> INTERFACE_MAP = new HashMap<>();

    public static Interface resolveInterface(final String clazz) {
        return INTERFACE_MAP.get(clazz);
    }

    public static VarDecl resolveField(final String callingClass, final String fieldOwner, final String name) {
        ClassDef def = CLASS_MAP.get(fieldOwner.replace(".", "/"));
        if (def != null) {
            String clazz = fieldOwner.replace(".", "/");
            while (!clazz.equals("java/lang/Object") && def != null) {
                VarDecl decl = findField(callingClass, clazz, name);
                if (decl != null) {
                    return decl;
                }
                clazz = def.getSuper().toString().replace(".", "/");
                def = CLASS_MAP.get(clazz);
            }
        }
        return null;
    }

    public static Fun resolveFun(final String callingClass, final String funOwner, final String name, final int paramCount) {
        ClassDef def = CLASS_MAP.get(funOwner.replace(".", "/"));
        if (def != null) {
            String clazz = funOwner.replace(".", "/");
            while (!clazz.equals("java/lang/Object") && def != null) {
                Fun decl = findFun(callingClass, clazz, name, paramCount);
                if (decl != null) {
                    return decl;
                }
                clazz = def.getSuper().toString().replace(".", "/");
                def = CLASS_MAP.get(clazz);
            }
        }
        return null;
    }

    public static Fun resolveJavaFun(final String funOwner, final String name, final int paramCount) {
        ClassDef def = CLASS_MAP.get(funOwner.replace(".", "/"));
        if (def != null) {
            String clazz = funOwner.replace(".", "/");
            while (!clazz.equals("java/lang/Object") && def != null) {
                Fun decl = findFun("", clazz, name, paramCount);
                if (decl != null && decl.isJavaMethod()) {
                    return decl;
                }
                clazz = def.getSuper().toString().replace(".", "/");
                def = CLASS_MAP.get(clazz);
            }
        }
        return null;
    }

    private static VarDecl findField(final String callingClass, final String funOwner, final String name) {
        ClassDef def = CLASS_MAP.get(funOwner.replace(".", "/"));
        if (def != null) {
            for (VarDecl field : def.getFields()) {
                if (name.equals(field.getName().toString())) {
                    for (Modifier modifier : field.getModifiers()) {
                        if (modifier.equals(Modifier.PRIVATE) && !callingClass.equals(funOwner)) {
                            return null;
                        }
                    }
                    return field;
                }
            }
        }
        return null;
    }

    private static Fun findFun(final String callingClass, final String funOwner, final String name, final int paramCount) {
        ClassDef def = CLASS_MAP.get(funOwner.replace(".", "/"));
        if (def != null) {
            for (Fun fun : def.getMethods()) {
                if (fun.getParams().length == paramCount && name.equals(fun.getName().toString())) {
                    for (Modifier modifier : fun.getModifiers()) {
                        if (modifier.equals(Modifier.PRIVATE) && !callingClass.equals(funOwner))
                            return null;
                    }
                    return fun;
                }
            }
        }
        return null;
    }

    public static void map(final ClassDef def) {
        if (def.isInterface()) {
            INTERFACE_MAP.put(def.getFullName(), (Interface) def);
        } else {
            CLASS_MAP.put(def.getFullName(), def);
        }
    }

    public static void map(final Class<?> clazz) {
        final String name = clazz.getName().replace(".", "/");

        if (CLASS_MAP.containsKey(name) || INTERFACE_MAP.containsKey(name))
            return;

        if (clazz.isInterface()) {
            INTERFACE_MAP.put(clazz.getName().replace(".", "/"), Interface.valueOf(clazz));
            return;
        }
        if (!clazz.equals(Object.class) && !clazz.getSuperclass().equals(Object.class))
            map(clazz.getSuperclass());
        QualifiedName[] interfaces = new QualifiedName[clazz.getInterfaces().length];
        for (int i = 0; i < clazz.getInterfaces().length; i++) {
            interfaces[i] = QualifiedName.valueOf(clazz.getInterfaces()[i].getName());
        }
        QualifiedName superClass = clazz.equals(Object.class) ? null : QualifiedName.valueOf(clazz.getSuperclass().getName());
        Inheritance inh = new Inheritance(superClass,
                new Expression[0], interfaces);
        List<Statement> statements = new ArrayList<>();
        ClassDef def = new ClassDef(new Modifier[0], clazz.getName(), inh, statements);

        for (Field field : clazz.getDeclaredFields()) {
            Modifier[] modifiers = new Modifier[0];
            if (java.lang.reflect.Modifier.isPublic(field.getModifiers()))
                modifiers = new Modifier[]{Modifier.PUBLIC};
            if (java.lang.reflect.Modifier.isPrivate(field.getModifiers()))
                modifiers = new Modifier[]{Modifier.PRIVATE};
            VarDecl decl = new VarDecl(QualifiedName.valueOf(field.getName()), null, modifiers);
            decl.setType("L" + field.getType().getName().replace(".", "/") + ";");
            statements.add(decl);
        }

        java.lang.reflect.Method[] gg = clazz.getDeclaredMethods();

        for (Method method : clazz.getDeclaredMethods()) {
            statements.add(Fun.valueOf(method));
        }

        CLASS_MAP.put(name, def);
    }
}
