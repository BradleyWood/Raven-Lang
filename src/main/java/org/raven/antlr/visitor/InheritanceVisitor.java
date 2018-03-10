package org.raven.antlr.visitor;

import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ast.Expression;
import org.raven.antlr.ast.Inheritance;
import org.raven.antlr.ast.QualifiedName;
import org.raven.compiler.ClassMaker;

import java.util.LinkedList;

public class InheritanceVisitor extends RavenBaseVisitor<Inheritance> {

    private InheritanceVisitor() {
    }

    @Override
    public Inheritance visitInheritance(RavenParser.InheritanceContext ctx) {
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
