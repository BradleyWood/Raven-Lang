package org.raven.antlr.visitor;

import org.junit.Test;
import org.raven.antlr.Operator;
import org.raven.antlr.ast.*;
import org.raven.core.wrappers.TInt;

import static org.raven.antlr.RuleTester.testStatement;

public class TernaryVisitorTest {

    @Test
    public void testTernary() {
        final TernaryOp tOp = new TernaryOp(new QualifiedName("a"), new QualifiedName("b"),
                new QualifiedName("c"));

        testStatement(ExpressionVisitor.INSTANCE, "a ? b : c", tOp);
    }

    @Test
    public void testTernary2() {
        final Expression condition = new BinOp(new Literal(new TInt(5)), Operator.GT, new Literal(new TInt(10)));
        final TernaryOp tOp = new TernaryOp(condition, new QualifiedName("b"), new QualifiedName("c"));

        testStatement(ExpressionVisitor.INSTANCE, "5 > 10 ? b : c", tOp);
    }
}
