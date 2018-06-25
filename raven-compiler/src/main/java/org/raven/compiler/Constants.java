package org.raven.compiler;

import org.raven.antlr.ast.QualifiedName;

public class Constants {

    public static final String TOBJ_SIG = "Lorg/raven/core/wrappers/TObject;";
    public static final String TOBJ_NAME = "org/raven/core/wrappers/TObject";

    public static final String BUILTIN_NAME = "raven/Builtin";
    public static final String ANNOTATION_TLFILE_SIG = "Lorg/raven/core/TLFile;";

    public static final QualifiedName[] COMMON_IMPORTS = {
            new QualifiedName("java", "lang", "Math"),
            new QualifiedName("java", "lang", "System"),
            new QualifiedName("org", "raven", "core", "JvmMethod")
    };

    public static final AnnotationProcessor[] DEFAULT_ANNOTATION_PROCESSORS = {
            new JvmMethodAnnotationProcessor()
    };

    static {
        for (QualifiedName commonImport : COMMON_IMPORTS) {
            try {
                SymbolMap.map(Class.forName(commonImport.toString()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
