package org.toylang.antlr.visitor;

import org.toylang.antlr.ToyLangBaseVisitor;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.ast.Annotation;
import org.toylang.antlr.ast.Expression;
import org.toylang.antlr.ast.QualifiedName;

public class AnnotationVisitor extends ToyLangBaseVisitor<Annotation> {

    private AnnotationVisitor() {
    }

    @Override
    public Annotation visitAnnotation(ToyLangParser.AnnotationContext ctx) {
        QualifiedName name = ctx.qualifiedName().accept(QualifiedNameVisitor.INSTANCE);
        Expression[] params = new Expression[0];

        if (ctx.paramDef() != null) {
            params = new Expression[ctx.paramDef().size()];
            for (int i = 0; i < params.length; i++) {
                params[i] = ctx.paramDef(i).accept(ExpressionVisitor.INSTANCE);
            }
        }
        return new Annotation(name.toString(), params);
    }

    public static final AnnotationVisitor INSTANCE = new AnnotationVisitor();
}
