package org.toylang.antlr;

import org.junit.Test;

import static org.toylang.antlr.RuleTester.*;

public class SyntaxTest {

    @Test
    public void testIf() {
        testSyntax("if a < b ;", true);

        testSyntax("{if a < b ;}", true);

        testSyntax("if a < b {}", true);

        testSyntax("if (a < b) {}", true);

        testSyntax("if (a < b) ;", true);

        testSyntax("if (((a < b))) ;", true);

        // no statement or block
        testSyntax("if (a < b)", false);

        testSyntax("( if (a < b) ; )", false);

        // random bracket - fail
        testSyntax("if a( < b ;", false);

        // no condition - fail
        testSyntax("if (());", false);
    }

    @Test
    public void testWhile() {
        testSyntax("while true ;", true);
        testSyntax("while a > b ;", true);
        testSyntax("while a > b {}", true);
        testSyntax("while (a > b) ;", true);
        testSyntax("while (((a > b))) ;", true);
        testSyntax("while (((a > b))) {}", true);
        testSyntax("{while (((a > b))) {}}", true);

        testSyntax("(while (((a > b))) {})", false);

        // mismatched brackets
        testSyntax("while (true ;", false);
        // no condition
        testSyntax("while () ;", false);
        testSyntax("while ;", false);
        // no statement or block
        testSyntax("while (true)", false);
    }
}
