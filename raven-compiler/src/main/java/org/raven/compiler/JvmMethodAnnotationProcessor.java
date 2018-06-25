package org.raven.compiler;

import org.raven.antlr.RavenTree;
import org.raven.antlr.ast.*;
import org.raven.core.wrappers.TString;
import org.raven.error.Errors;

import java.util.LinkedList;

/**
 * Creates an adaptor to a raven method with specified types
 * and performs coercion
 */
public class JvmMethodAnnotationProcessor implements AnnotationProcessor {

    private final Literal DEFAULT_PARAMS = new Literal(new TString(""));
    private final Literal DEFAULT_RET = new Literal(new TString("void"));

    @Override
    public void process(final Annotation annotation) {
        Statement stmt = (Statement) annotation.getParent();

        if (annotation.getName().equalsIgnoreCase("JvmMethod")) {
            if (!(stmt instanceof Fun)) {
                Errors.put("line " + annotation.getLineNumber() + ": Annotation: " + annotation.getName() +
                        " only applies to methods.");
                return;
            }
            Fun method = createJavaMethod((Fun) stmt, annotation);
            if (stmt.getParent() instanceof ClassDef) {
                ((ClassDef) stmt.getParent()).getStatements().add(method);
            } else if (stmt.getParent() instanceof RavenTree) {
                ((RavenTree) stmt.getParent()).addFunction(method);
            } else {
                Errors.put("line " + annotation.getLineNumber() + ": invalid parent for function: "
                        + stmt.getParent().getClass());
            }
        }
    }

    private Fun createJavaMethod(final Fun fun, final Annotation annotation) {
        Block body = new Block();

        Fun javaMethod = new Fun(fun.getName(), body, new LinkedList<>(), fun.getExceptions(), fun.getParams());
        fun.getModifiers().forEach(javaMethod::addModifier);

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
        fun.getAnnotations().forEach(javaMethod::addAnnotation);

        return javaMethod;
    }

    private void updateDesc(final Fun fun, final String params, final String ret) {
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

    private String getType(final String type) {
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
            case "Object":
                return "Ljava/lang/Object;";
            case "void":
                return "V";
            default:
                return "L" + type.replace(".", "/") + ";";
        }
    }
}
