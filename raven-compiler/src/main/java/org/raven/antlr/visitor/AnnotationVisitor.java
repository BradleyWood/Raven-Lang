package org.raven.antlr.visitor;

import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ast.Annotation;
import org.raven.antlr.ast.Literal;
import org.raven.antlr.ast.QualifiedName;

import java.util.Arrays;

public class AnnotationVisitor extends RavenBaseVisitor<Annotation> {

    private AnnotationVisitor() {
    }

    @Override
    public Annotation visitAnnotation(final RavenParser.AnnotationContext ctx) {
        QualifiedName name = ctx.qualifiedName().accept(QualifiedNameVisitor.INSTANCE);
        QualifiedName[] names = new QualifiedName[0];
        Literal[] params = new Literal[0];

        if (ctx.annotationParamList() != null) {
            params = new Literal[ctx.annotationParamList().annotationParam().size()];
            names = new QualifiedName[ctx.annotationParamList().annotationParam().size()];
            for (int i = 0; i < params.length; i++) {
                names[i] = ctx.annotationParamList().annotationParam(i).paramDef().accept(ParamDefVisitor.INSTANCE).getName();
                params[i] = (Literal) ctx.annotationParamList().annotationParam(i).literal().accept(LiteralVisitor.INSTANCE);
            }
        }

        Annotation annotation = new Annotation(name.toString(), names, params);

        name.setParent(annotation);
        Arrays.stream(names).forEach(qn -> qn.setParent(annotation));
        Arrays.stream(params).forEach(p -> p.setParent(annotation));

        annotation.setLineNumber(ctx.start.getLine());

        return annotation;
    }

    public static final AnnotationVisitor INSTANCE = new AnnotationVisitor();
}
