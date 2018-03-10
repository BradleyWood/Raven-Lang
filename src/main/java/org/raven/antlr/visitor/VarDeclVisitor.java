package org.raven.antlr.visitor;

import org.raven.antlr.Modifier;
import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ast.Expression;
import org.raven.antlr.ast.QualifiedName;
import org.raven.antlr.ast.VarDecl;

public class VarDeclVisitor extends RavenBaseVisitor<VarDecl> {

    private VarDeclVisitor() {
    }

    @Override
    public VarDecl visitVarDeclaration(RavenParser.VarDeclarationContext ctx) {
        QualifiedName name = new QualifiedName(ctx.IDENTIFIER().getText());
        Expression expr = null;

        if (ctx.expression() != null)
            expr = ctx.expression().accept(ExpressionVisitor.INSTANCE);
        Modifier[] modArray = new Modifier[0];

        if (ctx.modifier() != null) {
            modArray = new Modifier[ctx.modifier().size()];
            for (int i = 0; i < modArray.length; i++) {
                modArray[i] = Modifier.getModifier(ctx.modifier(i).getText());
            }
        }

        return new VarDecl(name, expr, modArray);
    }

    public static final VarDeclVisitor INSTANCE = new VarDeclVisitor();
}
