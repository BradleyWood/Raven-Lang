package org.toylang.antlr.visitor;

import org.toylang.antlr.ToyLangBaseVisitor;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.ast.QualifiedName;
import org.toylang.antlr.ast.VarDecl;

public class ParamDefVisitor extends ToyLangBaseVisitor<VarDecl> {

    private ParamDefVisitor() {
    }

    @Override
    public VarDecl visitParamDef(ToyLangParser.ParamDefContext ctx) {
        return new VarDecl(new QualifiedName(ctx.IDENTIFIER().getText()), null);
    }

    public static final ParamDefVisitor INSTANCE = new ParamDefVisitor();
}
