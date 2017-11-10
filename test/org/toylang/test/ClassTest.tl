package org.toylang.test;

import org.toylang.test.Assert;

import javax.swing.JFrame;

public class Ok(X, Y) {
    var Z = 100;

    public fun getZ() {
        return Z;
    }
    public fun setZ(z) {
        Z = z;
    }
}

fun testOk() {
    var gg = Ok(10, 20);
    Assert.assertEqual(10, gg.getX());
    Assert.assertEqual(20, gg.getY());
    Assert.assertEqual(100, gg.getZ());
}

public class SubClass extends Ok(100, 100) {}

fun testSubClass() {
    var gg = SubClass();
    Assert.assertEqual(100, gg.getX());
    Assert.assertEqual(100, gg.getY());
    Assert.assertEqual(100, gg.getZ());
    gg.setZ(200);
    Assert.assertEqual(200, gg.getZ());
    gg.setY(300);
    Assert.assertEqual(300, gg.getY());
    gg.setX(400);
    Assert.assertEqual(400, gg.getX());
}

public class SuperClass(x) {}

public class Tester extends JFrame {

    public constructor(a) {
        super(a);
    }

    public fun updateSize() {
        setSize(500, 600);
        Assert.assertEqual(10, ClassTest.aFun());
    }
}

fun aFun() {
    return 10;
}

fun testInheritance() {
    var gg = Tester("ABCDEFG");
    gg.setSize(400, 401);
    Assert.assertEqual(400, gg.getWidth());
    Assert.assertEqual(401, gg.getHeight());

    gg.updateSize();

    Assert.assertEqual(500, gg.getWidth());
    Assert.assertEqual(600, gg.getHeight());

    Assert.assertEqual("ABCDEFG", gg.getTitle());
}
