package org.toylang.compiler;

import org.toylang.core.ToyObject;

import java.util.LinkedList;
import java.util.List;

public class Constants {

    public static final String TOYOBJ_SIG = "Lorg/toylang/core/ToyObject;";
    public static final String TOYOBJ_NAME = "org/toylang/core/ToyObject";

    public static final String BUILTIN_NAME = "toylang/lang/Builtin";
    public static final String ANNOTATION_TLFILE_SIG = "Lorg/toylang/core/TLFile;";

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
