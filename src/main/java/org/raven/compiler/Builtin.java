package org.raven.compiler;

import org.raven.antlr.ast.QualifiedName;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class Builtin {

    private static ArrayList<Builtin> builtins = new ArrayList<>();

    private String name;
    private int numParams;

    public Builtin(final String name, final int numParams) {
        this.name = name;
        this.numParams = numParams;
    }

    public static boolean isBuiltin(final QualifiedName name, final int numParams) {
        if (builtins.size() == 0) {
            loadBuiltins();
        }
        for (Builtin builtin : builtins) {
            if (builtin.name.equals(name.toString()) && builtin.numParams == numParams)
                return true;
        }
        return false;
    }

    private static void loadBuiltins() {
        try {
            Class<?> builtin = Class.forName("raven.Builtin");
            SymbolMap.map(builtin);

            for (Method method : builtin.getMethods()) {
                builtins.add(new Builtin(method.getName(), method.getParameterCount()));
            }
        } catch (ClassNotFoundException e) {
        }
    }
}
