package org.toylang.antlr.visitor;

import org.junit.Test;
import org.toylang.antlr.ast.Literal;
import org.toylang.core.wrappers.*;

import static org.toylang.antlr.RuleTester.testStatement;

public class LiteralVisitorTest {

    @Test
    public void testIntegerLiterals() {
        Literal literal = new Literal(new TInt(400444444));
        testStatement(LiteralVisitor.INSTANCE, "400444444;", literal);

        literal = new Literal(new TInt(0));
        testStatement(LiteralVisitor.INSTANCE, "0;", literal);

        literal = new Literal(new TInt(1000000));
        testStatement(LiteralVisitor.INSTANCE, "1e6;", literal);

        // can't test negatives here -- test under unary operator expressions
    }

    @Test
    public void testBooleanLiterals() {
        Literal literal = new Literal(TBoolean.TRUE);
        testStatement(LiteralVisitor.INSTANCE, "true;", literal);

        literal = new Literal(TBoolean.FALSE);
        testStatement(LiteralVisitor.INSTANCE, "false;", literal);
    }

    @Test
    public void testNullLiteral() {
        Literal literal = new Literal(TNull.NULL);
        testStatement(LiteralVisitor.INSTANCE, "null;", literal);
    }

    @Test
    public void testBigIntLiterals() {
        Literal literal = new Literal(new TBigInt("1231249852524243224"));
        testStatement(LiteralVisitor.INSTANCE, "1231249852524243224;", literal);
    }

    @Test
    public void testRealLiterals() {
        Literal literal = new Literal(new TReal(1.5e6));
        testStatement(LiteralVisitor.INSTANCE, "1.5e6;", literal);

        literal = new Literal(new TReal(0.0));
        testStatement(LiteralVisitor.INSTANCE, "0.0;", literal);

        literal = new Literal(new TReal(Math.PI));
        testStatement(LiteralVisitor.INSTANCE, String.valueOf(Math.PI) + ";", literal);
    }

}
