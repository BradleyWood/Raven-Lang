package org.toylang.test;

import org.junit.Assert;
import org.toylang.core.wrappers.TInt;
import org.toylang.core.wrappers.TBigInt;
import org.toylang.repl.Repl;

fun replTestVar() {
    var repl = Repl();
    repl.exec("import org.junit.Assert;");
    repl.exec("var a = 100;");
    repl.exec("Assert.assertEquals(a, 100);");
}

fun testVar2() {
    var repl = Repl();
    repl.exec("import org.junit.Assert;");
    repl.exec("var a = 100;");
    repl.exec("Assert.assertEquals(a, 100);");
    repl.exec("var a = 200;");
    repl.exec("Assert.assertEquals(a, 200);");
}

fun testFun() {
    var repl = Repl();
    repl.exec("import org.junit.Assert;");
    repl.exec("f(x) = x * x + x + 1;");
    repl.exec("Assert.assertEquals(f(10), 111);");
}
