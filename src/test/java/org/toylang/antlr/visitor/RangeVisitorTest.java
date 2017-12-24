package org.toylang.antlr.visitor;

import org.junit.Test;
import org.toylang.antlr.Operator;
import org.toylang.antlr.ast.BinOp;
import org.toylang.antlr.ast.Literal;
import org.toylang.antlr.ast.QualifiedName;
import org.toylang.antlr.ast.Range;
import org.toylang.core.wrappers.TInt;

import static org.toylang.antlr.RuleTester.testStatement;

public class RangeVisitorTest {

    @Test
    public void testIncreasingIntRange() {
        Range range = new Range(new Literal(new TInt(0)), new Literal(new TInt(10)));

        testStatement(RangeVisitor.INSTANCE, "range 0 to 10", range);

        testStatement(RangeVisitor.INSTANCE, "range 0 upto 10", range);
    }

    @Test
    public void testDecreasingIntRange() {
        Range range = new Range(new Literal(new TInt(10)), new Literal(new TInt(0)));

        testStatement(RangeVisitor.INSTANCE, "range 10 downto 0", range);
    }

    @Test
    public void testVarRange() {
        Range range = new Range(new QualifiedName("a"), new QualifiedName("b"));

        testStatement(RangeVisitor.INSTANCE, "range a to b", range);
        testStatement(RangeVisitor.INSTANCE, "range a upto b", range);

        range = new Range(new QualifiedName("b"), new QualifiedName("a"));
        testStatement(RangeVisitor.INSTANCE, "range b downto a", range);
    }

    @Test
    public void testRangeWithExpression() {
        Range range = new Range(new BinOp(new QualifiedName("a"), Operator.ADD, new Literal(new TInt(1))),
                new BinOp(new QualifiedName("b"), Operator.MULT, new Literal(new TInt(40))));

        testStatement(RangeVisitor.INSTANCE, "range a + 1 to b * 40", range);
        testStatement(RangeVisitor.INSTANCE, "range a + 1 upto b * 40", range);
    }
}
