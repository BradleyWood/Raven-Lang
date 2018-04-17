package org.raven.antlr.visitor;

import org.raven.antlr.Modifier;
import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ast.*;

import java.util.ArrayList;

public class MethodDeclVisitor extends RavenBaseVisitor<Fun> {

    private MethodDeclVisitor() {
    }

    @Override
    public Fun visitMethodDeclaration(final RavenParser.MethodDeclarationContext ctx) {
        QualifiedName name = new QualifiedName(ctx.IDENTIFIER().getText());
        Block body;
        if (ctx.block() != null) {
            body = ctx.block().accept(BlockVisitor.INSTANCE);
        } else if (ctx.expression() != null) {
            Return stmt = new Return(ctx.expression().accept(ExpressionVisitor.INSTANCE));
            body = new Block(stmt);
        } else {
            System.err.println("ERROR: NO BODY FOR FUNCTION: " + name);
            return null;
        }


        ArrayList<VarDecl> params = new ArrayList<>();
        if (ctx.paramDef() != null) {
            ctx.paramDef().forEach(pd -> params.add(pd.accept(ParamDefVisitor.INSTANCE)));
        }
        Modifier[] modArray = new Modifier[0];

        if (ctx.modifier() != null) {
            modArray = new Modifier[ctx.modifier().size()];
            for (int i = 0; i < modArray.length; i++) {
                modArray[i] = Modifier.getModifier(ctx.modifier(i).getText());
            }
        }

        Fun fun = new Fun(name, body, modArray, new String[0], params.toArray(new VarDecl[params.size()]));

        if (ctx.annotation() != null) {
            for (RavenParser.AnnotationContext annotationContext : ctx.annotation()) {
                Annotation anno = annotationContext.accept(AnnotationVisitor.INSTANCE);
                fun.addAnnotation(anno);
            }
        }

        return fun;
    }

    public static final MethodDeclVisitor INSTANCE = new MethodDeclVisitor();
}
