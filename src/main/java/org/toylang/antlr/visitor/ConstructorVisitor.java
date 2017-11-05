package org.toylang.antlr.visitor;

import org.toylang.antlr.Modifier;
import org.toylang.antlr.ToyLangBaseVisitor;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.ast.Annotation;
import org.toylang.antlr.ast.Block;
import org.toylang.antlr.ast.Constructor;
import org.toylang.antlr.ast.VarDecl;

import java.util.ArrayList;

public class ConstructorVisitor extends ToyLangBaseVisitor<Constructor> {

    private ConstructorVisitor() {
    }

    @Override
    public Constructor visitConstructor(ToyLangParser.ConstructorContext ctx) {

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
