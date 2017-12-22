package org.toylang.antlr;

import org.antlr.v4.runtime.*;
import org.junit.Assert;
import org.toylang.antlr.ToyLangLexer;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.visitor.ToyFileVisitor;

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
}
