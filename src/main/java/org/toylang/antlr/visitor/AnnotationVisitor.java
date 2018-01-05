package org.toylang.antlr.visitor;

import org.toylang.antlr.ToyLangBaseVisitor;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.ast.Annotation;
import org.toylang.antlr.ast.Literal;
import org.toylang.antlr.ast.QualifiedName;

public class AnnotationVisitor extends ToyLangBaseVisitor<Annotation> {

    private AnnotationVisitor() {
    }

    @Override
    public Annotation visitAnnotation(ToyLangParser.AnnotationContext ctx) {
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
        return new Annotation(name.toString(), names, params);
    }

    public static final AnnotationVisitor INSTANCE = new AnnotationVisitor();
}
