package org.toylang.compiler;

import org.toylang.core.ToyObject;

import java.util.LinkedList;
import java.util.List;

public class Constants {

    public static final String TOYOBJ_SIG = "Lorg/toylang/core/ToyObject;";
    public static final String TOYOBJ_NAME = "org/toylang/core/ToyObject";
    public static final String TOYSTRING_SIG = "Lorg/toylang/core/ToyString;";

    public static final String TOYNULL_NAME = "org/toylang/core/ToyNull";
    public static final String TOYSTRING_NAME = "org/toylang/core/ToyString";

    public static final String BUILTIN_NAME = "toylang/lang/Builtin";

    public static final String CLASS_SIG = "Ljava/lang/Class;";

    public static final String STRING_SIG = "Ljava/lang/String;";

    public static final String TOY_DICT_NAME = "org/toylang/core/ToyDict";
    public static final String TOY_DICT_SIG = "Lorg/toylang/core/ToyDict;";

    public static final String TOY_ERROR_NAME = "org/toylang/core/ToyError";
    public static final String TOY_ERROR_SIG = "Lorg/toylang/core/ToyError;";

    public static final String TOY_BOOLEAN_NAME = "org/toylang/core/ToyBoolean";
    public static final String TOY_BOOLEAN_SIG = "Lorg/toylang/core/ToyBoolean;";

    private static LinkedList<ToyObject> CONSTANTS = new LinkedList<>();

    public static void addConstant(ToyObject literal) {
        CONSTANTS.add(literal);
    }
    public static List<ToyObject> getConstants() {
        return CONSTANTS;
    }
    public static int getConstantCount() {
        return CONSTANTS.size();
    }
    public static void clear() {
        CONSTANTS.clear();
    }
}
