package org.toylang.compiler;

import org.toylang.antlr.ToyTree;
import org.toylang.antlr.ast.*;
import org.toylang.core.wrappers.TString;
import org.toylang.error.Errors;

public class JvmMethodAnnotationProcessor implements AnnotationProcessor {

    private final Literal DEFAULT_PARAMS = new Literal(new TString(""));
    private final Literal DEFAULT_RET = new Literal(new TString("void"));

    @Override
    public void process(ToyTree file, Statement stmt) {
        if (!(stmt instanceof Fun)) {
            return;
        }
        for (Annotation annotation : stmt.getAnnotations()) {
            if (annotation.getName().equalsIgnoreCase("JvmMethod")) {
                if (file.getStatements().contains(stmt)) {
                    file.getStatements().add(createJavaMethod((Fun) stmt, annotation));
                }
                for (ClassDef classDef : file.getClasses()) {
                    if (classDef.getMethods().contains(stmt)) {
                        classDef.getStatements().add(createJavaMethod((Fun) stmt, annotation));
                    }
                }
            }
        }
    }

    private Fun createJavaMethod(Fun fun, Annotation annotation) {
        Block body = new Block();
        Fun javaMethod = new Fun(fun.getName(), body, fun.getModifiers(), fun.getExceptions(), fun.getParams());
        if (annotation.get("name") != null) {
            javaMethod.setName(annotation.get("name").getValue().toString());
        }
        Literal params = annotation.getOrDefault("params", DEFAULT_PARAMS);
        Literal ret = annotation.getOrDefault("ret", DEFAULT_RET);
        updateDesc(javaMethod, params.getValue().toString(),
                ret.getValue().toString());

        Expression[] jParams = new Expression[fun.getParams().length];
        for (int i = 0; i < jParams.length; i++) {
            jParams[i] = new QualifiedName(fun.getParams()[i].getName().toString());
        }
        Call call = new Call(fun.getName(), jParams);

        if (ret.equals(DEFAULT_RET))
            call.setPop(true);

        body.addBefore(call);

        return javaMethod;
    }

    private void updateDesc(Fun fun, String params, String ret) {
        String[] types = params.replace(" ", "").split(",");
        if (params.length() == 0)
            types = new String[0];
        if (types.length != fun.getParams().length) {
            Errors.put("adaptor must have same number of params as target");
        }

        StringBuilder builder = new StringBuilder("(");
        for (String type : types) {
            builder.append(getType(type));
        }
        builder.append(")").append(getType(ret));

        fun.forceDescriptor(builder.toString());
    }

    private String getType(String type) {
        switch (type) {
            case "int":
                return "I";
            case "long":
                return "J";
            case "short":
                return "S";
            case "boolean":
                return "Z";
            case "byte":
                return "B";
            case "float":
                return "F";
            case "double":
                return "D";
            case "String":
                return "Ljava/lang/String;";
            case "void":
                return "V";
            default:
                return "L" + type.replace(".", "/") + ";";
        }
    }
}
