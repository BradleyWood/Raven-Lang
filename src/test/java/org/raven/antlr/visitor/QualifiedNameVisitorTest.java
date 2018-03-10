package org.raven.antlr.visitor;

import org.junit.Test;
import org.raven.antlr.ast.QualifiedName;

import static org.raven.antlr.RuleTester.testStatement;

public class QualifiedNameVisitorTest {

    @Test
    public void qNameTest() {
        QualifiedName name = new QualifiedName("someName");
        testStatement(QualifiedNameVisitor.INSTANCE, "someName", name);
    }

    @Test
    public void qNameTest2() {
        QualifiedName name = new QualifiedName("someName", "anotherName", "finally");
        testStatement(QualifiedNameVisitor.INSTANCE, "someName.anotherName.finally", name);
    }
}
