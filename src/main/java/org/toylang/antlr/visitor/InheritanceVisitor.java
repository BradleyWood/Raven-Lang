package org.toylang.antlr.visitor;

import org.toylang.antlr.ToyLangBaseVisitor;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.ast.Expression;
import org.toylang.antlr.ast.Inheritance;
import org.toylang.antlr.ast.QualifiedName;
import org.toylang.compiler.ClassMaker;

import java.util.LinkedList;

public class InheritanceVisitor extends ToyLangBaseVisitor<Inheritance> {

    private InheritanceVisitor() {
    }

    @Override
    public Inheritance visitInheritance(ToyLangParser.InheritanceContext ctx) {
        QualifiedName superClass = ClassMaker.OBJECT;
        LinkedList<Expression> superParams = new LinkedList<>();

        if (ctx.ext() != null) {
            superClass = ctx.ext().qualifiedName().accept(QualifiedNameVisitor.INSTANCE);
            if (ctx.ext().paramList() != null) {

                ctx.ext().paramList().param().forEach(q -> superParams.add(q.accept(ExpressionVisitor.INSTANCE)));
            }
        }

        LinkedList<QualifiedName> interfaces = new LinkedList<>();
        if (ctx.impl() != null) {
            if (ctx.impl().interfaceList() != null) {
                ctx.impl().interfaceList().qualifiedName().forEach(q -> interfaces.add(q.accept(QualifiedNameVisitor.INSTANCE)));
            }
        }
        return new Inheritance(superClass, superParams.size() == 0 ? null : superParams.toArray(new Expression[superParams.size()]),
                interfaces.toArray(new QualifiedName[interfaces.size()]));
    }

    public static InheritanceVisitor INSTANCE = new InheritanceVisitor();

}
