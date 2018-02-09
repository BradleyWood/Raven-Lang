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
        testSyntax(" for i = 0; i < 10; i += 2, j +=1 {}", true);
        testSyntax(" for i = 0; i < 10; i += 2, j +=1, k += 3;", true);

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
        testSyntax("var true = b;", false);
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
        testSyntax("class SoMeClAsS() {}", true);
        testSyntax("class SoMeClAsS { constructor(a,b,c){} }", true);
        testSyntax("class SoMeClAsS extends AnOtHeRcLaSs {}", true);
        testSyntax("class SoMeClAsS extends AnOtHeRcLaSs implements GG {}", true);
        testSyntax("class SoMeClAsS(a,b) extends AnOtHeRcLaSs implements GG { constructor(a,b,c){} }", true);
        testSyntax("class SoMeClAsS(a,b) extends AnOtHeRcLaSs implements GG {}", true);
        testSyntax("class SoMeClAsS(a,b) extends AnOtHeRcLaSs(b,a) implements GG {}", true);

        testSyntax("class SoMeClAsS;", false);
        testSyntax("class SoMeClAsS( {}", false);
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

    @Test
    public void testListDef() {
        testSyntax("[1, 50, 100, 2000, 50000];", true);
        testSyntax("[1, \"str\", 100, 2000, 50000];", true);
        testSyntax("var a = [1, 50, 100, 2000, 50000];", true);

        testSyntax("[1, 50, 100, 2000, 50000]", false);
        testSyntax("[1, 50, 100, 2000, 50000,,,,];", false);
    }

    @Test
    public void testListIdx() {
        testSyntax("a[5];", true);
        testSyntax("a[5];", true);
        testSyntax("a[5][5][\"5\"][5][\"str\"];", true);

        testSyntax("a[5]", false);
        testSyntax("a[];", false);
        testSyntax("a[5][\"str\"][5][5][][5];", false);
    }

    @Test
    public void testDictionaryDef() {
        testSyntax("{1 : 2, 50 : 100, \"str\" : \"msg\", 2000 : 100, 50000 : 10};", true);
        testSyntax("{\"a\" : \"b\"};", true);

        testSyntax("{\"a\" : \"b\";", false);
        testSyntax("{\"a\", \"b\"};", false);
        testSyntax("{1 : 2, 2};", false);
        testSyntax("{1 : 2,,, 2 : 2};", false);
        testSyntax("{1 : , 2 : 4};", false);
        testSyntax("{1 : 2, : 2};", false);
    }

    @Test
    public void testSlice() {
        testSyntax("a[:];", true);
        testSyntax("a[1:];", true);
        testSyntax("a[:2];", true);
        testSyntax("a[1:4];", true);
        testSyntax("a[a:];", true);
        testSyntax("a[:b];", true);
        testSyntax("a[a:b];", true);

        testSyntax("a[1:5:10];", false);
        testSyntax("a[1:2]", false);
        testSyntax("a[]", false);
    }

    @Test
    public void testFunctionCall() {
        testSyntax("testFun();", true);
        testSyntax("testFun(a);", true);
        testSyntax("testFun(a,b,c,d,e,f,g,h,i);", true);
        testSyntax("testFun(1,2,4,true,false,\"str\", a,b);", true);

        testSyntax("test()", false);
        testSyntax("testFun(a, b)", false);
        testSyntax("testFun(a,, b);", false);
        testSyntax("testFun(a, b,,);", false);
        testSyntax("testFun(,,a, b);", false);
        testSyntax("testFun(,);", false);
    }

    @Test
    public void testGo() {
        testSyntax("go toHell();", true);
        testSyntax("go toHell(a,b,c,d);", true);
        testSyntax("go toHell(1,2,3,4);", true);
        testSyntax("go id.toHell();", true);

        testSyntax("go true;", false);
        testSyntax("go 412;", false);
        testSyntax("go toHell()", false);
        testSyntax("go toHell(,);", false);
        testSyntax("go toHell(1,);", false);
        testSyntax("go toHell(1,,4);", false);
        testSyntax("go toHell(,1,4);", false);
    }

    @Test
    public void testAnnotationDef() {
        testSyntax("@interface Id {}", true);
        testSyntax("@interface Id {a,b,c}", true);

        testSyntax("@interface Id (a,b,c)", false);
        testSyntax("@interface Id (true,b,c)", false);
        testSyntax("@interface Id (2,b,c)", false);
        testSyntax("@interface Id {a,b,c,,}", false);
        testSyntax("@interface Id {,}", false);
        testSyntax("@interface Id {a,}", false);
        testSyntax("@interface true {}", false);
    }

    @Test
    public void testAnnotatedMethods() {
        testSyntax("@JvmMethod(name=\"method\", params=\"int, int, String\", ret=\"int\")" +
                "fun anAnnotatedMethod(a, b, c) {}", true);

        testSyntax("@SomeAnnotation\n" +
                "fun anAnnotatedMethod() {}", true);

        testSyntax("@SomeAnnotation()\n" +
                "fun anAnnotatedMethod() {}", false);

        testSyntax("@SomeAnnotation(a=b+45)\n" +
                "fun anAnnotatedMethod() {}", false);

        testSyntax("@SomeAnnotation(a=[5,6])\n" +
                "fun anAnnotatedMethod() {}", false);

        testSyntax("@SomeAnnotation(\"msg\")\n" +
                "fun anAnnotatedMethod() {}", false);

        testSyntax("@SomeAnnotation(a=,b=5)\n" +
                "fun anAnnotatedMethod() {}", false);

        testSyntax("@SomeAnnotation(5, 7, 1)\n" +
                "fun anAnnotatedMethod() {}", false);

        testSyntax("@SomeAnnotation(a, b, c)\n" +
                "fun anAnnotatedMethod() {}", false);

        testSyntax("@SomeAnnotation(a=b, c=d, d=e)\n" +
                "fun anAnnotatedMethod() {}", false);

        testSyntax("@SomeAnnotation(a=5, b=\"c\"\n" +
                "fun anAnnotatedMethod() {}", false);
    }

    @Test
    public void testInterfaceDef() {
        testSyntax("interface TestInterface {}", true);
        testSyntax("@Test interface TestInterface {}", true);
        testSyntax("@Test public interface TestInterface {}", true);
        testSyntax("private interface TestInterface {}", true);
        testSyntax("@Test private interface TestInterface {}", true);
        testSyntax("@Test @Cool private interface TestInterface {}", true);

        testSyntax("interface TestInterface { fun test(); }", true);
        testSyntax("interface TestInterface { public fun test(); }", true);
        testSyntax("@Test public interface TestInterface { public fun test(); }", true);
        testSyntax("interface TestInterface { public fun test(); fun someFun(a, b, c); }", true);

        testSyntax("interface TestInterface { private fun test(); }", false);
        testSyntax("interface TestInterface { private fun test() }", false);
        testSyntax("interface TestInterface { private fun test(; }", false);
        testSyntax("interface TestInterface { private test(); }", false);
        testSyntax("interface TestInterface  private test(); ", false);
        testSyntax("interface TestInterface {{ private test(); }}", false);
    }
}
