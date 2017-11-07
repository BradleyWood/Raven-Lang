package org.toylang.antlr.visitor;

import org.toylang.antlr.Modifier;
import org.toylang.antlr.ToyLangBaseVisitor;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.ast.Expression;
import org.toylang.antlr.ast.QualifiedName;
import org.toylang.antlr.ast.VarDecl;

public class VarDeclVisitor extends ToyLangBaseVisitor<VarDecl> {

    private VarDeclVisitor() {
    }

    @Override
    public VarDecl visitVarDeclaration(ToyLangParser.VarDeclarationContext ctx) {
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
