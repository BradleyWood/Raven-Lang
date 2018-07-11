package org.raven.antlr.visitor;

import org.junit.Test;
import org.raven.antlr.ast.*;
import org.raven.core.wrappers.TBoolean;
import org.raven.core.wrappers.TInt;
import org.raven.core.wrappers.TNull;

import static org.raven.antlr.RuleTester.testStatement;

public class WhenVisitorTest {

    @Test
    public void testWhen() {
        final Case[] cases = new Case[2];
        cases[0] = new Case(new Literal(new TInt(10)), new Block(new Literal(new TInt(100))));
        cases[1] = new Case(new QualifiedName("b"), new Block(new QualifiedName("b")));

        final When when = new When(new QualifiedName("x"), cases, new Block(new Literal(new TInt(1000))));

        testStatement(WhenVisitor.INSTANCE, "when (x) { 10->100 \n b->b \n else -> 1000 }", when);
    }

    @Test
    public void testWhenWithBlock() {
        final Case[] cases = new Case[2];
        cases[0] = new Case(new Literal(new TInt(10)), new Block(new Literal(TNull.NULL)));

        final When when = new When(new QualifiedName("x"), cases, new Block(new Literal(TBoolean.TRUE)));

        testStatement(WhenVisitor.INSTANCE, "when (x) { 10->{} \n else -> { true } }", when);
    }

    @Test
    public void testWhenWithMultiStmtBlock() {
        final Case[] cases = new Case[2];
        cases[0] = new Case(new Literal(new TInt(10)), new Block(new Literal(TNull.NULL)));

        final Expression expr = new Literal(new TInt(10));
        expr.setPop(true);
        final When when = new When(new QualifiedName("x"), cases, new Block(expr, new Literal(TBoolean.TRUE)));

        testStatement(WhenVisitor.INSTANCE, "when (x) { 10->{} \n else -> { 10\n true } }", when);
    }

    @Test
    public void testWhenEmptyBlock() {
        final Case[] cases = new Case[2];
        cases[0] = new Case(new Literal(new TInt(10)), new Block(new Literal(TNull.NULL)));

        final When when = new When(new QualifiedName("x"), cases, new Block(new Literal(TNull.NULL)));

        testStatement(WhenVisitor.INSTANCE, "when (x) { 10->{} \n else -> {} }", when);
    }

    @Test
    public void testWhenNoCondition() {
        final Case[] cases = new Case[2];
        cases[0] = new Case(new Literal(new TInt(10)), new Block(new Literal(TNull.NULL)));

        final When when = new When(null, cases, new Block(new Literal(TNull.NULL)));

        testStatement(WhenVisitor.INSTANCE, "when { 10->{} \n else -> {} }", when);
    }
}
