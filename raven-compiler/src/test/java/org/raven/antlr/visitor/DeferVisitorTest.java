package org.raven.antlr.visitor;

import org.junit.Test;
import org.raven.antlr.ast.Call;
import org.raven.antlr.ast.Defer;
import org.raven.antlr.ast.Expression;
import org.raven.antlr.ast.QualifiedName;

import static org.raven.antlr.RuleTester.testStatement;

public class DeferVisitorTest {

    @Test
    public void testBasicDeferment() {
        final Defer defer = new Defer(new Call(new QualifiedName("call")));

        testStatement(DeferVisitor.INSTANCE, "defer call()", defer);
    }

    @Test
    public void testDeferOnInstanceMethod() {
        final Defer defer = new Defer(new Call(new QualifiedName("call")));
        defer.getCall().setPrecedingExpr(new QualifiedName("a"));

        testStatement(DeferVisitor.INSTANCE, "defer a.call()", defer);
    }

    @Test
    public void testDeferWithParameters() {
        final Expression[] parameters = {
                new QualifiedName("a"),
                new QualifiedName("b")
        };
        final Defer defer = new Defer(new Call(new QualifiedName("call"), parameters));
        defer.getCall().setPrecedingExpr(new QualifiedName("a"));

        testStatement(DeferVisitor.INSTANCE, "defer a.call(a, b)", defer);
    }
}
