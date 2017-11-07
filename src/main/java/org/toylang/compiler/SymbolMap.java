package org.toylang.compiler;

import org.toylang.antlr.ast.ClassDef;
import org.toylang.antlr.ast.Fun;
import org.toylang.antlr.ast.VarDecl;

import java.util.HashMap;

public class SymbolMap {

    public static HashMap<String, VarDecl> VARIABLE_MAP = new HashMap<>();
    public static HashMap<String, Fun> FUN_MAP = new HashMap<>();
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
}
