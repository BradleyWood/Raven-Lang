package org.toylang.antlr.visitor;

import org.toylang.antlr.Modifier;
import org.toylang.antlr.ToyLangBaseVisitor;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.ast.*;

import java.util.ArrayList;
import java.util.List;

public class ClassDefVisitor extends ToyLangBaseVisitor<ClassDef> {

    private ClassDefVisitor() {

    }
    @Override
    public ClassDef visitClassDef(ToyLangParser.ClassDefContext ctx) {
        Modifier[] modifiers = new Modifier[0];
        String super_ = "java/lang/Object";
        String[] interfaces = new String[0];

        if(ctx.modifier() != null) {
            modifiers = new Modifier[ctx.modifier().size()];
            for(int i = 0; i < modifiers.length; i++) {
                modifiers[i] = Modifier.getModifier(ctx.modifier(i).getText());
            }
        }
        String name = ctx.IDENTIFIER().getText();

        List<Statement> statementList = new ArrayList<>();

        if(ctx.paramList() != null) {
            if(ctx.paramList().size() > 0) {
                for (int i = 0; i < ctx.paramList(0).param().size(); i++) {
                    VarDecl decl = new VarDecl(new QualifiedName(ctx.paramList(0).param(i).getText()), null, Modifier.PRIVATE);
                    statementList.add(decl);

                }
            }
            if(ctx.paramList().size() > 1) {
                int size = ctx.paramList(1).param().size();
                if(size > 0) {
                    super_ = ctx.paramList(1).param(0).getText();
                    interfaces = new String[size - 1];
                    for (int i = 1; i < size; i++) {
                        interfaces[i - 1] = ctx.paramList(1).param(i).getText();
                    }
                }
            }
        }

        Block block = ctx.block().accept(BlockVisitor.INSTANCE);
        statementList.addAll(block.getStatements());

        return new ClassDef(modifiers, name, new QualifiedName(super_), interfaces, statementList);
    }
    public static ClassDefVisitor INSTANCE = new ClassDefVisitor();
}
