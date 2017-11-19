package org.toylang.compiler;

import org.toylang.antlr.ast.ClassDef;
import org.toylang.antlr.ast.Fun;
import org.toylang.antlr.ast.VarDecl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class SymbolMap {

    public static HashMap<String, VarDecl> VARIABLE_MAP = new HashMap<>();
    public static HashMap<String, Fun> FUN_MAP = new HashMap<>();
    public static HashMap<String, List<Fun>> LOCAL_FUNCTIONS = new HashMap<>();
    public static HashMap<String, ClassDef> CLASS_MAP = new HashMap<>();

    public static VarDecl resolveVar(String file, String name) {
        String QName = file + "." + name;
        if (QName.contains("/")) {
            QName = QName.replaceAll("/", ".");
        }
        for (String s : VARIABLE_MAP.keySet()) {
            if (s.equals(QName)) {
                return VARIABLE_MAP.get(s);
            }
        }
        return null;
    }

    private static String getName(String file, String name) {
        String QName = file + "." + name;
        if (QName.contains("/")) {
            QName = QName.replaceAll("/", ".");
        }
        return QName;
    }

    public static Fun resolveFun(String callingClass, String funOwner, String name, int paramCount) {
        if (funOwner.equals(callingClass)) {
            for (String s : LOCAL_FUNCTIONS.keySet()) {
                if (s.equals(callingClass + "." + name)) {
                    for (Fun fun : LOCAL_FUNCTIONS.get(s)) {
                        if (fun.getName().toString().equals(name)) {
                            return fun;
                        }
                    }
                }
            }
        }
        return resolveFun(funOwner, name, paramCount);
    }

    public static Fun resolveFun(String file, String name) {
        String QName = getName(file, name);
        for (String s : FUN_MAP.keySet()) {
            if (s.equals(QName)) {
                return FUN_MAP.get(s);
            }
        }
        return null;
    }

    public static Fun resolveFun(String file, String name, int numParams) {
        String QName = getName(file, name);
        for (String s : FUN_MAP.keySet()) {
            Fun f = FUN_MAP.get(s);
            if (s.equals(QName) && f.getParams().length == numParams) {
                return f;
            }
        }
        return null;
    }

    public static ClassDef resolveClass(String file, String name) {
        String QName = getName(file, name);
        for (String s : CLASS_MAP.keySet()) {
            if (s.equals(QName)) {
                return CLASS_MAP.get(s);
            }
        }
        return null;
    }

    public static void addLocalFun(String owner, Fun fun) {
        if (LOCAL_FUNCTIONS.containsKey(owner)) {
            LOCAL_FUNCTIONS.get(owner).add(fun);
        } else {
            LOCAL_FUNCTIONS.put(owner, new LinkedList<>());
            addLocalFun(owner, fun);
        }
    }
}
