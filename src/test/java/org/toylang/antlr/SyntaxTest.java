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

    @Test
    public void testVarDecl() {
        testSyntax("var a;", true);
        testSyntax("var a = 10;", true);
        testSyntax("var a = \"str\";", true);
        testSyntax("var a = true;", true);
        testSyntax("var a = false;", true);
        testSyntax("var a = b;", true);

        testSyntax("var a", false);
        testSyntax("var a =", false);
        testSyntax("var a = ;", false);
        testSyntax("var a = 100", false);
        testSyntax("var a = true", false);
        testSyntax("var a = b", false);
    }

    @Test
    public void testBinaryOp() {
        testSyntax("a * b;", true);
        testSyntax("a / b;", true);
        testSyntax("10 + b;", true);
        testSyntax("10 - 10;", true);
        testSyntax("a % b;", true);
        testSyntax("a ** b;", true);
        testSyntax("a += b;", true);
        testSyntax("a -= b;", true);
        testSyntax("a *= b;", true);
        testSyntax("a /= b;", true);
        testSyntax("a %= b;", true);
        testSyntax("a **= b;", true);
        testSyntax("a && b;", true);
        testSyntax("a || b;", true);

        // binary op with unary op
        testSyntax("a =- b;", true);
        testSyntax("a =+ b;", true);

        testSyntax("a =/ b;", false);
        testSyntax("a =% b;", false);
        testSyntax("a =* b;", false);
        testSyntax("a =** b;", false);

        testSyntax("a b;", false);
        testSyntax("a b c;", false);
        testSyntax("a true b;", false);
    }

    @Test
    public void testUnaryOp() {
        testSyntax("!b;", true);
        testSyntax("!!b;", true);
        testSyntax("!!!!!b;", true);
        testSyntax("-b;", true);
        testSyntax("+b;", true);
        testSyntax("-------b;", true);
        testSyntax("----+-+-+--b;", true);

        testSyntax("!(b);", true);
        testSyntax("-+!(!!--++--(!+--+b));", true);

        testSyntax("/b;", false);
        testSyntax("*b;", false);
        testSyntax("%b;", false);
    }

    @Test
    public void testImport() {
        testSyntax("import javax.swing.JFrame;", true);

        testSyntax("import a..b.c.d;", false);
        testSyntax("import a.....b.c..d;", false);

        testSyntax("import ", false);
        testSyntax("import ;", false);

        testSyntax("import javax.swing.JFrame", false);
    }

    @Test
    public void testFun() {
        testSyntax("f() = 50;", true);
        testSyntax("f(x,y,z) = x + y + z;", true);
        testSyntax("fun f() = 50;", true);

        testSyntax("fun f() {}", true);
        testSyntax("fun f(x) {}", true);
        testSyntax("fun f() {;;;;;;;;}", true);

        testSyntax("fun f() ;", false);
        testSyntax("fun f(x,y,z) ;", false);
        testSyntax("fun f(5, 1, 6) {}", false);
        testSyntax("fun f(true, false, 5, for) {}", false);
        testSyntax("fun f(,,) {}", false);
    }

    @Test
    public void testClass() {
        testSyntax("class SoMeClAsS(a, b, c) {}", true);
        testSyntax("class SoMeClAsS {}", true);
        testSyntax("class SoMeClAsS { constructor(a,b,c){} }", true);
        testSyntax("class SoMeClAsS extends AnOtHeRcLaSs {}", true);
        testSyntax("class SoMeClAsS extends AnOtHeRcLaSs implements GG {}", true);
        testSyntax("class SoMeClAsS(a,b) extends AnOtHeRcLaSs implements GG { constructor(a,b,c){} }", true);
        testSyntax("class SoMeClAsS(a,b) extends AnOtHeRcLaSs implements GG {}", true);
        testSyntax("class SoMeClAsS(a,b) extends AnOtHeRcLaSs(b,a) implements GG {}", true);

        testSyntax("class SoMeClAsS;", false);
        testSyntax("class SoMeClAsS( {}", false);
        testSyntax("class SoMeClAsS() {}", false);
        testSyntax("class SoMeClAsS extends {}", false);
        testSyntax("class SoMeClAsS extends AnOtHeRcLaSs implements {}", false);
        testSyntax("class SoMeClAsS(a,b) extends AnOtHeRcLaSs() implements GG() {}", false);
    }

    @Test
    public void testBreak() {
        testSyntax("break;", true);

        testSyntax("break", false);
    }

    @Test
    public void testContinue() {
        testSyntax("continue;", true);

        testSyntax("continue", false);
    }
}
