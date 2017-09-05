package org.toylang.compiler;

import org.toylang.antlr.ast.QualifiedName;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class Builtin {

    private static ArrayList<Builtin> builtins = new ArrayList<>();

    static {
        try {
            Class<?> builtin = Class.forName("toylang.lang.Builtin");
            for (Method method : builtin.getMethods()) {
                builtins.add(new Builtin(method.getName(), method.getParameterCount()));
            }
        } catch (ClassNotFoundException e) {
        }
    }

    private String name;
    private int numParams;

    public Builtin(String name, int numParams) {
        this.name = name;
        this.numParams = numParams;
    }
    public static boolean isBuiltin(QualifiedName name, int numParams) {
        for (Builtin builtin : builtins) {
            if(builtin.name.equals(name.toString()) && builtin.numParams == numParams)
                return true;
        }
        return false;
    }

}
