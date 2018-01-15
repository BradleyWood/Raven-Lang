package org.toylang.compiler;

import org.toylang.antlr.Modifier;
import org.toylang.antlr.ast.*;
import org.toylang.core.wrappers.TObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class SymbolMap {

    private static HashMap<String, ClassDef> CLASS_MAP = new HashMap<>();

    public static VarDecl resolveField(String callingClass, String funOwner, String name) {
        ClassDef def = CLASS_MAP.get(funOwner);
        if (def != null) {
            String clazz = funOwner;
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

    public static Fun resolveFun(String callingClass, String funOwner, String name, int paramCount) {
        ClassDef def = CLASS_MAP.get(funOwner);
        if (def != null) {
            String clazz = funOwner;
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

    private static VarDecl findField(String callingClass, String funOwner, String name) {
        ClassDef def = CLASS_MAP.get(funOwner);
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

    private static Fun findFun(String callingClass, String funOwner, String name, int paramCount) {
        ClassDef def = CLASS_MAP.get(funOwner);
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

    public static void map(ClassDef def) {
        CLASS_MAP.put(def.getFullName(), def);
    }

    public static void map(Class<?> clazz) {
        if (!clazz.getSuperclass().equals(Object.class))
            map(clazz.getSuperclass());
        QualifiedName[] interfaces = new QualifiedName[clazz.getInterfaces().length];
        for (int i = 0; i < clazz.getInterfaces().length; i++) {
            interfaces[i] = QualifiedName.valueOf(clazz.getInterfaces()[i].getName());
        }
        Inheritance inh = new Inheritance(QualifiedName.valueOf(clazz.getSuperclass().getName()),
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
            boolean isJavaField = !field.getType().equals(TObject.class);
            decl.setJavaField(isJavaField);
            statements.add(decl);
        }

        for (Method method : clazz.getDeclaredMethods()) {
            statements.add(Fun.valueOf(method));
        }

        CLASS_MAP.put(clazz.getName().replace(".", "/"), def);
    }

    public static void output() {
        for (Map.Entry<String, ClassDef> stringClassDefEntry : CLASS_MAP.entrySet()) {
            System.out.println("-----CLASS: " + stringClassDefEntry.getKey() + " ------");
            ClassDef def = stringClassDefEntry.getValue();
            for (VarDecl varDecl : def.getFields()) {
                System.out.println("Field: " + varDecl.getName());
            }
            for (Fun fun : def.getMethods()) {
                System.out.println("Method: " + fun.getName().toString() + " paramsCount=" + fun.getParams().length);
            }
        }
    }
}
