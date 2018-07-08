package org.raven.antlr;

import org.antlr.v4.runtime.*;
import org.junit.Assert;
import org.raven.antlr.ast.Expression;
import org.raven.antlr.ast.Import;
import org.raven.antlr.ast.Range;
import org.raven.antlr.ast.Statement;
import org.raven.antlr.visitor.InheritanceVisitor;
import org.raven.antlr.visitor.ParamDefVisitor;
import org.raven.antlr.visitor.RavenFileVisitor;
import org.raven.error.Errors;

import static org.junit.Assert.assertEquals;

public class RuleTester {

    /**
     * Tests input for syntax errors
     *
     * @param txt The input
     * @param pass Whether there should or shouldn't be syntax errors
     */
    public static void testSyntax(final String txt, final boolean pass) {
        RavenLexer lexer = new RavenLexer(CharStreams.fromString(txt));
        CommonTokenStream ts = new CommonTokenStream(lexer);
        RavenParser p = new RavenParser(ts);
        p.removeErrorListener(ConsoleErrorListener.INSTANCE);

        try {
            p.ravenFile();
        } catch (Exception ignored) {
            if (pass) {
                Assert.fail();
            } else {
                return;
            }
        }

        if (pass) {
            Assert.assertEquals("Syntax Errors for: " + txt, 0, p.getNumberOfSyntaxErrors());
        } else {
            Assert.assertNotEquals("Expected syntax errors: " + txt, 0, p.getNumberOfSyntaxErrors());
        }
    }

    /**
     * Tests whether the output produced by the parser matches the expected ast node
     * @param v The visitor responsible for handling the parsing rule
     * @param txt The input text
     * @param expected The expected output
     */
    public static void testStatement(final RavenBaseVisitor v, final String txt, final Object expected) {
        RavenLexer lexer = new RavenLexer(CharStreams.fromString(txt));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        RavenParser parser = new RavenParser(tokenStream);
        parser.removeErrorListener(ConsoleErrorListener.INSTANCE);
        Object stmt;
        if (expected instanceof Range) {
            stmt = v.visit(parser.range());
        } else if (expected instanceof Import) {
            stmt = v.visit(parser.importStatement());
        } else if (expected instanceof Expression) {
            stmt = v.visit(parser.expression());
        } else if (v instanceof ParamDefVisitor) {
            stmt = v.visit(parser.paramDef());
        } else if (v instanceof InheritanceVisitor) {
            stmt = v.visitInheritance(parser.inheritance());
        } else {
            stmt = (Statement) v.visit(parser.statement());
        }
        assertEquals(expected, stmt);
    }
}
