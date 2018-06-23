package org.raven.antlr.visitor;

import org.junit.Test;
import org.raven.antlr.Operator;
import org.raven.antlr.ast.*;
import org.raven.core.wrappers.TBoolean;
import org.raven.core.wrappers.TInt;
import org.raven.core.wrappers.TString;

import static org.raven.antlr.RuleTester.testStatement;

public class WhileVisitorTest {

    private static final Block EMPTY_BLOCK = new Block();
    private static final Block TEST_BLOCK1 = new Block();
    private static final Block TEST_BLOCK2 = new Block();

    static {
        TEST_BLOCK1.append(new BinOp(new QualifiedName("i"), Operator.ASSIGNMENT,
                new BinOp(new QualifiedName("i"), Operator.ADD, new Literal(new TInt(1)))));

        Call call = new Call(new QualifiedName("println"), new Literal(new TString("msg")));
        call.setPop(true);
        TEST_BLOCK2.append(call);
    }

    @Test
    public void testLoopNoBody() {
        While whileLoop = new While(new Literal(TBoolean.TRUE), EMPTY_BLOCK);
        testStatement(WhileVisitor.INSTANCE, "while (true) {}", whileLoop);
    }

    @Test
    public void testBlock1() {
        While whileLoop = new While(
                new BinOp(new QualifiedName("i"),
                        Operator.LT,
                new Literal(new TInt(10))), TEST_BLOCK1);
        testStatement(WhileVisitor.INSTANCE, "while (i < 10) { i = i + 1; }", whileLoop);
    }

    @Test
    public void testBlock2() {
        While whileLoop = new While(
                new BinOp(new QualifiedName("i"),
                        Operator.LT,
                        new Literal(new TInt(10))), TEST_BLOCK2);
        testStatement(WhileVisitor.INSTANCE, "while (i < 10) { println(\"msg\"); }", whileLoop);
    }
}
