package org.toylang.antlr.visitor;

import org.toylang.antlr.Modifier;
import org.toylang.antlr.ToyLangBaseVisitor;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.ast.*;
import org.toylang.compiler.ClassMaker;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ClassDefVisitor extends ToyLangBaseVisitor<ClassDef> {

    private ClassDefVisitor() {

    }

    @Override
    public ClassDef visitClassDef(ToyLangParser.ClassDefContext ctx) {
        Modifier[] modifiers = new Modifier[0];
        QualifiedName super_ = ClassMaker.OBJECT;

        if (ctx.modifier() != null) {
            modifiers = new Modifier[ctx.modifier().size()];
            for (int i = 0; i < modifiers.length; i++) {
                modifiers[i] = Modifier.getModifier(ctx.modifier(i).getText());
            }
        }
        String name = ctx.IDENTIFIER().getText();

        LinkedList<VarDecl> varParams = new LinkedList<>();

        if (ctx.fields != null) {
            for (int i = 0; i < ctx.fields.param().size(); i++) {
                VarDecl decl = new VarDecl(new QualifiedName(ctx.fields.param(i).getText()), null, Modifier.PRIVATE);
                varParams.add(decl);
            }
        }
        Inheritance inh = new Inheritance(super_, null, null);
        if (ctx.inheritance() != null) {
            inh = ctx.inheritance().accept(InheritanceVisitor.INSTANCE);
        }

        Block block = ctx.block().accept(BlockVisitor.INSTANCE);
        List<Statement> statementList = new ArrayList<>(block.getStatements());

        ClassDef def = new ClassDef(modifiers, name, inh, statementList);
        def.setVarParams(varParams);
        return def;
    }

    public static ClassDefVisitor INSTANCE = new ClassDefVisitor();
}
