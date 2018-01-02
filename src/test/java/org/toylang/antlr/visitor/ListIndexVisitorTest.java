package org.toylang.antlr.visitor;

import org.junit.Test;
import org.toylang.antlr.Operator;
import org.toylang.antlr.ast.BinOp;
import org.toylang.antlr.ast.ListIndex;
import org.toylang.antlr.ast.Literal;
import org.toylang.antlr.ast.QualifiedName;
import org.toylang.core.wrappers.*;

import static org.toylang.antlr.RuleTester.testStatement;

/**
 * These tests also apply to the associative array
 */
public class ListIndexVisitorTest {


    @Test
    public void testLiteralKey() {

        ListIndex listIndex = new ListIndex(new QualifiedName("lst"), new Literal(new TInt(5)));
        testStatement(ListIndexVisitor.INSTANCE, "lst[5];", listIndex);

        listIndex = new ListIndex(new QualifiedName("lst"), new Literal(new TInt(-5)));
        testStatement(ListIndexVisitor.INSTANCE, "lst[-5];", listIndex);

        listIndex = new ListIndex(new QualifiedName("lst"), new Literal(new TString("abc")));
        testStatement(ListIndexVisitor.INSTANCE, "lst[\"abc\"];", listIndex);

        listIndex = new ListIndex(new QualifiedName("lst"), new Literal(TNull.NULL));
        testStatement(ListIndexVisitor.INSTANCE, "lst[null];", listIndex);

        listIndex = new ListIndex(new QualifiedName("lst"), new Literal(TBoolean.TRUE));
        testStatement(ListIndexVisitor.INSTANCE, "lst[true];", listIndex);

        listIndex = new ListIndex(new QualifiedName("lst"), new Literal(new TReal(0.5)));
        testStatement(ListIndexVisitor.INSTANCE, "lst[0.5];", listIndex);
    }

    @Test
    public void testVarKey() {
        ListIndex listIndex = new ListIndex(new QualifiedName("lst"), new QualifiedName("aKey"));
        testStatement(ListIndexVisitor.INSTANCE, "lst[aKey];", listIndex);
    }

    @Test
    public void testExpressionKey() {
        ListIndex listIndex = new ListIndex(new QualifiedName("lst"),
                new BinOp(new Literal(new TInt(1)), Operator.ADD, new Literal(new TInt(5))));
        testStatement(ListIndexVisitor.INSTANCE, "lst[1 + 5];", listIndex);
    }

    @Test
    public void test2DIndices() {
        ListIndex listIndex = new ListIndex(new QualifiedName("lst"),
                new QualifiedName("aKey"), new Literal(new TInt(5)));
        testStatement(ListIndexVisitor.INSTANCE, "lst[aKey][5];", listIndex);
    }

    @Test
    public void test4DIndices() {
        ListIndex listIndex = new ListIndex(new QualifiedName("lst"),
                new QualifiedName("aKey"), new Literal(new TInt(5)),
                new Literal(TBoolean.TRUE),
                new QualifiedName("anotherKey"));
        testStatement(ListIndexVisitor.INSTANCE, "lst[aKey][5][true][anotherKey];", listIndex);
    }
}
