package org.raven.antlr.visitor;

import org.junit.Test;
import org.raven.antlr.Modifier;
import org.raven.antlr.ast.*;
import org.raven.core.wrappers.TInt;

import java.util.Collections;
import java.util.LinkedList;

import static org.raven.antlr.RuleTester.testStatement;

public class ClassDefVisitorTest {

    private static final QualifiedName OBJECT = new QualifiedName("java", "lang", "Object");
    private static final Inheritance DEFAULT_INHERITANCE = new Inheritance(OBJECT, new Expression[0], new QualifiedName[0]);

    @Test
    public void testEmptyClass() {
        final ClassDef def = new ClassDef(new Modifier[0], "aClass", DEFAULT_INHERITANCE, Collections.emptyList());

        testStatement(ClassDefVisitor.INSTANCE, "class aClass {}", def);
    }

    @Test
    public void testInheritance() {
        final Inheritance inh = new Inheritance(new QualifiedName("a", "b", "c"), null, new QualifiedName[0]);
        final ClassDef def = new ClassDef(new Modifier[0], "aClass", inh, Collections.emptyList());

        testStatement(ClassDefVisitor.INSTANCE, "class aClass extends a.b.c {}", def);
    }

    @Test
    public void testInterfaces() {
        final Inheritance inh = new Inheritance(OBJECT, null, new QualifiedName[] {
                new QualifiedName("a", "b", "c"),
                new QualifiedName("d", "e", "f")
        });

        final ClassDef def = new ClassDef(new Modifier[0], "aClass", inh, Collections.emptyList());

        testStatement(ClassDefVisitor.INSTANCE, "class aClass implements a.b.c, d.e.f {}", def);
    }

    @Test
    public void testBody() {
        final LinkedList<Statement> statements = new LinkedList<>();
        final ClassDef def = new ClassDef(new Modifier[0], "aClass", DEFAULT_INHERITANCE, statements);

        statements.add(new VarDecl(new QualifiedName("a"), new Literal(new TInt(5))));

        final Block functionBody = new Block(new Return(new Literal(new TInt(100))));
        statements.add(new Fun(new QualifiedName("aFun"), functionBody, new Modifier[0], new String[0]));

        testStatement(ClassDefVisitor.INSTANCE, "class aClass { var a = 5\r\nfun aFun() { return 100 } }", def);
        testStatement(ClassDefVisitor.INSTANCE, "class aClass { var a = 5;fun aFun() { return 100 } }", def);
    }
}
