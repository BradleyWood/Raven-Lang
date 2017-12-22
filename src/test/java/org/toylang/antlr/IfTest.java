package org.toylang.antlr;

import org.junit.Test;

import static org.toylang.antlr.RuleTester.*;

public class IfTest {

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
}
