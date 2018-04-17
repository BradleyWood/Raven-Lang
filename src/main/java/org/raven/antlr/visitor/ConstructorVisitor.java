package org.raven.antlr.visitor;

import org.raven.antlr.Modifier;
import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ast.Annotation;
import org.raven.antlr.ast.Block;
import org.raven.antlr.ast.Constructor;
import org.raven.antlr.ast.VarDecl;

import java.util.ArrayList;

public class ConstructorVisitor extends RavenBaseVisitor<Constructor> {

    private ConstructorVisitor() {
    }

    @Override
    public Constructor visitConstructor(final RavenParser.ConstructorContext ctx) {

        Annotation[] annotations = new Annotation[0];
        if (ctx.annotation() != null) {
            annotations = new Annotation[ctx.annotation().size()];
            for (int i = 0; i < annotations.length; i++) {
                annotations[i] = ctx.annotation(i).accept(AnnotationVisitor.INSTANCE);
            }
        }

        Modifier[] modifiers = new Modifier[0];
        if (ctx.modifier() != null) {
            modifiers = new Modifier[ctx.modifier().size()];
            for (int i = 0; i < modifiers.length; i++) {
                modifiers[i] = Modifier.getModifier(ctx.modifier(i).getText());
            }
        }

        Block body = ctx.block().accept(BlockVisitor.INSTANCE);

        ArrayList<VarDecl> params = new ArrayList<>();
        if (ctx.paramDef() != null) {
            ctx.paramDef().forEach(pd -> params.add(pd.accept(ParamDefVisitor.INSTANCE)));
        }

        return new Constructor(modifiers, body, params.toArray(new VarDecl[params.size()]));
    }

    public static final ConstructorVisitor INSTANCE = new ConstructorVisitor();

}
