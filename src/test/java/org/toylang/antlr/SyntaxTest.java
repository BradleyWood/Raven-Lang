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

        testSyntax("while", false);
    }

    @Test
    public void testFor() {
        testSyntax(" for ;; ;", true);
        testSyntax(" for ;; {}", true);
        testSyntax(" for (;;) ;", true);
        testSyntax(" for (;;) {}", true);
        testSyntax(" for (i = 0; i < 10; i += 2) ;", true);
        testSyntax(" for (i = 0; i < 10; i += 2) {}", true);
        testSyntax(" for i = 0; i < 10; i += 2 ;", true);
        testSyntax(" for i = 0; i < 10; i += 2 {}", true);

        testSyntax("for ;", false);
        testSyntax("for (;;)", false);
        testSyntax("for (;;;)", false);
        testSyntax("for", false);
    }

    @Test
    public void testForRanges() {
        testSyntax("for i range 0 to 0 ;", true);
        testSyntax("for (i range 0 to 0) ;", true);
        testSyntax("for i range 0 upto 0 ;", true);
        testSyntax("for i range 0 downto 0 ;", true);
        testSyntax("for i range 0 to 0 {}", true);

        testSyntax("for range 0 to 0 ;", false);
        testSyntax("for i range (0 to 0) ;", false);

        testSyntax("for i (range 0 to 0) ;", false);
        testSyntax("for i range (0 to 0) ;", false);
        testSyntax("for i a range 0 to 0 ;", false);
        testSyntax("for a : b range 0 to 0 ;", false);
        testSyntax("for ;; range 0 to 0 ;", false);
        testSyntax("for range 0 to 0 {}", false);
    }

    @Test
    public void testForEach() {
        testSyntax("for a : b ;", true);
        testSyntax("for a : b {}", true);
        testSyntax("for ((a : b)) ;", true);
        testSyntax("for ((a : b)) {}", true);
        testSyntax("for (a : b) {}", true);

        testSyntax("for a : b range a to b;", false);
        testSyntax("for (range a to b) a : b ;", false);
        testSyntax("for : ;;", false);
        testSyntax("for a: ;;", false);
        testSyntax("for :b ;;", false);
    }
}
