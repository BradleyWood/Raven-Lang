package org.raven.antlr.visitor;

import org.raven.antlr.Modifier;
import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ast.*;
import org.raven.compiler.ClassMaker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ClassDefVisitor extends RavenBaseVisitor<ClassDef> {

    private ClassDefVisitor() {

    }

    @Override
    public ClassDef visitClassDef(final RavenParser.ClassDefContext ctx) {
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
        Inheritance inh = new Inheritance(super_, new Expression[0], new QualifiedName[0]);
        if (ctx.inheritance() != null) {
            inh = ctx.inheritance().accept(InheritanceVisitor.INSTANCE);
        }

        Block block = ctx.block().accept(BlockVisitor.INSTANCE);
        List<Statement> statementList = new ArrayList<>(block.getStatements());

        ClassDef def = new ClassDef(modifiers, name, inh, statementList);

        def.setVarParams(varParams);
        block.setParent(def);

        inh.getSuperClass().setParent(def);
        Arrays.stream(inh.getInterfaces()).forEach(iFace -> iFace.setParent(def));

        Expression[] superParams = inh.getSuperParams();
        if (superParams != null)
            Arrays.stream(superParams).forEach(sp -> sp.setParent(def));

        return def;
    }

    public static ClassDefVisitor INSTANCE = new ClassDefVisitor();
}
