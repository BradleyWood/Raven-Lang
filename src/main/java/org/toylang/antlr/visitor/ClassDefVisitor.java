package org.toylang.antlr.visitor;

import org.toylang.antlr.Modifier;
import org.toylang.antlr.ToyLangBaseVisitor;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.ast.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ClassDefVisitor extends ToyLangBaseVisitor<ClassDef> {

    private ClassDefVisitor() {

    }

    @Override
    public ClassDef visitClassDef(ToyLangParser.ClassDefContext ctx) {
        Modifier[] modifiers = new Modifier[0];
        String super_ = "java/lang/Object";
        String[] interfaces = new String[0];

        if (ctx.modifier() != null) {
            modifiers = new Modifier[ctx.modifier().size()];
            for (int i = 0; i < modifiers.length; i++) {
                modifiers[i] = Modifier.getModifier(ctx.modifier(i).getText());
            }
        }
        String name = ctx.IDENTIFIER().getText();

        List<Statement> statementList = new ArrayList<>();

        LinkedList<VarDecl> varParams = new LinkedList<>();

        if (ctx.fields != null) {
            for (int i = 0; i < ctx.fields.param().size(); i++) {
                VarDecl decl = new VarDecl(new QualifiedName(ctx.fields.param(i).getText()), null, Modifier.PRIVATE);
                varParams.add(decl);
            }
        }
        if (ctx.impl != null) {
            int size = ctx.impl.param().size();
            if (size > 0) {
                super_ = ctx.impl.param(0).getText();
                interfaces = new String[size - 1];
                for (int i = 1; i < size; i++) {
                    interfaces[i - 1] = ctx.impl.param(i).getText();
                }
            }
        }

        Block block = ctx.block().accept(BlockVisitor.INSTANCE);
        statementList.addAll(block.getStatements());

        ClassDef def = new ClassDef(modifiers, name, new QualifiedName(super_), interfaces, statementList);
        def.setVarParams(varParams);
        return def;
    }

    public static ClassDefVisitor INSTANCE = new ClassDefVisitor();
}
