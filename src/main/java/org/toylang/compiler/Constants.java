package org.toylang.compiler;

import org.toylang.antlr.ast.QualifiedName;
import org.toylang.core.wrappers.TObject;

import java.util.LinkedList;
import java.util.List;

public class Constants {

    public static final String TOBJ_SIG = "Lorg/toylang/core/wrappers/TObject;";
    public static final String TOBJ_NAME = "org/toylang/core/wrappers/TObject";

    public static final String BUILTIN_NAME = "toylang/Builtin";
    public static final String ANNOTATION_TLFILE_SIG = "Lorg/toylang/core/TLFile;";

    private static LinkedList<TObject> CONSTANTS = new LinkedList<>();

    public static void addConstant(TObject literal) {
        CONSTANTS.add(literal);
    }

    public static final QualifiedName[] COMMON_IMPORTS = {
            new QualifiedName("java", "lang", "Math"),
            new QualifiedName("java", "lang", "System"),
    };

    public static List<TObject> getConstants() {
        return CONSTANTS;
    }

    public static int getConstantCount() {
        return CONSTANTS.size();
    }

    public static void clear() {
        CONSTANTS.clear();
    }
}
