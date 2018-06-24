package org.raven.antlr.visitor;

import org.junit.Test;
import org.raven.antlr.Operator;
import org.raven.antlr.ast.BinOp;
import org.raven.antlr.ast.ListIndex;
import org.raven.antlr.ast.Literal;
import org.raven.antlr.ast.QualifiedName;
import org.raven.core.wrappers.*;

import static org.raven.antlr.RuleTester.testStatement;

/**
 * These tests also apply to the associative array
 */
public class ListIndexVisitorTest {


    @Test
    public void testLiteralKey() {
        ListIndex listIndex = new ListIndex(new QualifiedName("lst"), new Literal(new TInt(5)));
        testStatement(ExpressionVisitor.INSTANCE, "lst[5];", listIndex);

        listIndex = new ListIndex(new QualifiedName("lst"), new Literal(new TInt(-5)));
        testStatement(ExpressionVisitor.INSTANCE, "lst[-5];", listIndex);

        listIndex = new ListIndex(new QualifiedName("lst"), new Literal(new TString("abc")));
        testStatement(ExpressionVisitor.INSTANCE, "lst[\"abc\"];", listIndex);

        listIndex = new ListIndex(new QualifiedName("lst"), new Literal(TNull.NULL));
        testStatement(ExpressionVisitor.INSTANCE, "lst[null];", listIndex);

        listIndex = new ListIndex(new QualifiedName("lst"), new Literal(TBoolean.TRUE));
        testStatement(ExpressionVisitor.INSTANCE, "lst[true];", listIndex);

        listIndex = new ListIndex(new QualifiedName("lst"), new Literal(new TReal(0.5)));
        testStatement(ExpressionVisitor.INSTANCE, "lst[0.5];", listIndex);
    }

    @Test
    public void testVarKey() {
        ListIndex listIndex = new ListIndex(new QualifiedName("lst"), new QualifiedName("aKey"));
        testStatement(ExpressionVisitor.INSTANCE, "lst[aKey];", listIndex);
    }

    @Test
    public void testExpressionKey() {
        ListIndex listIndex = new ListIndex(new QualifiedName("lst"),
                new BinOp(new Literal(new TInt(1)), Operator.ADD, new Literal(new TInt(5))));
        testStatement(ExpressionVisitor.INSTANCE, "lst[1 + 5];", listIndex);
    }

    @Test
    public void test2DIndices() {
        ListIndex listIndex = new ListIndex(new QualifiedName("lst"),
                new QualifiedName("aKey"), new Literal(new TInt(5)));
        testStatement(ExpressionVisitor.INSTANCE, "lst[aKey][5];", listIndex);
    }

    @Test
    public void test4DIndices() {
        ListIndex listIndex = new ListIndex(new QualifiedName("lst"),
                new QualifiedName("aKey"), new Literal(new TInt(5)),
                new Literal(TBoolean.TRUE),
                new QualifiedName("anotherKey"));
        testStatement(ExpressionVisitor.INSTANCE, "lst[aKey][5][true][anotherKey];", listIndex);
    }
}
