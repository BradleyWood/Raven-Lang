package org.toylang.test;

import org.toylang.test.Assert;
import org.toylang.core.wrappers.TInt;
import org.toylang.core.wrappers.TBigInt;
import org.toylang.repl.Repl;

fun replTestVar() {
    var repl = Repl();
    repl.exec("import org.toylang.test.Assert;");
    repl.exec("var a = 100;");
    repl.exec("Assert.assertEqual(a, 100);");
}

fun testVar2() {
    var repl = Repl();
    repl.exec("import org.toylang.test.Assert;");
    repl.exec("var a = 100;");
    repl.exec("Assert.assertEqual(a, 100);");
    repl.exec("var a = 200;");
    repl.exec("Assert.assertEqual(a, 200);");
}

fun testFun() {
    var repl = Repl();
    repl.exec("import org.toylang.test.Assert;");
    repl.exec("f(x) = x * x + x + 1;");
    repl.exec("Assert.assertEqual(f(10), 111);");
}
