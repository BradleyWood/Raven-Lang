package org.toylang.antlr.visitor;

import org.junit.Test;
import org.toylang.antlr.ast.QualifiedName;

import static org.toylang.antlr.RuleTester.testStatement;

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
