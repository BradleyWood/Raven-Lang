package org.raven.antlr.visitor;

import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ast.QualifiedName;
import org.raven.antlr.ast.VarDecl;

public class ParamDefVisitor extends RavenBaseVisitor<VarDecl> {

    private ParamDefVisitor() {
    }

    @Override
    public VarDecl visitParamDef(final RavenParser.ParamDefContext ctx) {
        QualifiedName name = new QualifiedName(ctx.IDENTIFIER().getText());

        VarDecl declaration = new VarDecl(new QualifiedName(ctx.IDENTIFIER().getText()), null);
        name.setParent(declaration);

        return declaration;
    }

    public static final ParamDefVisitor INSTANCE = new ParamDefVisitor();
}
