package org.toylang.antlr;

import org.antlr.v4.runtime.*;
import org.junit.Assert;
import org.toylang.antlr.ToyLangLexer;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.ToyLangBaseVisitor;
import org.toylang.antlr.ast.Expression;
import org.toylang.antlr.ast.Statement;
import org.toylang.antlr.visitor.ToyFileVisitor;

import static org.junit.Assert.assertEquals;

public class RuleTester {

    /**
     * Tests input for syntax errors
     *
     * @param txt The input
     * @param pass Whether there should or shouldn't be syntax errors
     */
    public static void testSyntax(String txt, boolean pass) {
        ToyLangLexer lexer = new ToyLangLexer(CharStreams.fromString(txt));
        CommonTokenStream ts = new CommonTokenStream(lexer);
        ToyLangParser p = new ToyLangParser(ts);
        p.removeErrorListener(ConsoleErrorListener.INSTANCE);

        try {
            new ToyFileVisitor().visit(p.toyFile());
        } catch (Exception e) {
            if (pass) {
                Assert.fail();
            } else {
                return;
            }
        }

        if (pass) {
            Assert.assertEquals(0, p.getNumberOfSyntaxErrors());
        } else {
            Assert.assertNotEquals(0, p.getNumberOfSyntaxErrors());
        }
    }

    /**
     * Tests whether the output produced by the parser matches the expected ast node
     * @param v The visitor responsible for handling the parsing rule
     * @param txt The input text
     * @param expected The expected output
     */
    public static void testStatement(ToyLangBaseVisitor v, String txt, Statement expected) {
        ToyLangLexer lexer = new ToyLangLexer(CharStreams.fromString(txt));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        ToyLangParser parser = new ToyLangParser(tokenStream);
        parser.removeErrorListener(ConsoleErrorListener.INSTANCE);
        Statement stmt;
        if (expected instanceof Expression) {
            stmt = (Statement) v.visit(parser.expression());
        } else {
            stmt = (Statement) v.visit(parser.statement());
        }
        assertEquals(expected, stmt);
    }
}
