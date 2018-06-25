package org.raven.antlr.visitor;

import org.junit.Test;
import org.raven.antlr.ast.Expression;
import org.raven.antlr.ast.Inheritance;
import org.raven.antlr.ast.Literal;
import org.raven.antlr.ast.QualifiedName;
import org.raven.core.wrappers.TInt;

import static org.raven.antlr.RuleTester.testStatement;

public class InheritanceVisitorTest {

    private static final QualifiedName OBJECT = new QualifiedName("java", "lang", "Object");
    private static final QualifiedName ABC = new QualifiedName("a", "b", "c");

    @Test
    public void testInheritance() {
        final Inheritance inh = new Inheritance(ABC, new Expression[0], new QualifiedName[0]);

        testStatement(InheritanceVisitor.INSTANCE, "extends a.b.c", inh);
    }

    @Test
    public void testInterfaces() {
        final Inheritance inh = new Inheritance(OBJECT, new Expression[0], new QualifiedName[] {
                new QualifiedName("abc"),
                new QualifiedName("d", "e", "f")
        });

        testStatement(InheritanceVisitor.INSTANCE, "implements abc, d.e.f", inh);
    }

    @Test
    public void testExtendsAndImplements() {
        final Inheritance inh = new Inheritance(ABC, new Expression[0], new QualifiedName[] {
                new QualifiedName("def"),
                new QualifiedName("g", "h", "i")
        });

        testStatement(InheritanceVisitor.INSTANCE, "extends a.b.c implements def, g.h.i", inh);
    }

    @Test
    public void testSuperArgs() {
        Inheritance inh = new Inheritance(new QualifiedName("abc"), new Expression[] {
                new Literal(new TInt(0))
        }, new QualifiedName[0]);

        testStatement(InheritanceVisitor.INSTANCE, "extends abc(0)", inh);

        inh = new Inheritance(new QualifiedName("abc"), new Expression[] {
                new QualifiedName("test")
        }, new QualifiedName[] {
                new QualifiedName("def"),
                new QualifiedName("g", "h", "i")
        });

        testStatement(InheritanceVisitor.INSTANCE, "extends abc(test) implements def, g.h.i", inh);
    }
}
