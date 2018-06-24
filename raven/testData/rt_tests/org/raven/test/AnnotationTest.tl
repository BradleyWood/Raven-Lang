package org.raven.test

import javax.swing.JOptionPane
import org.junit.Assert
import org.raven.test.AnnotationTest

@interface JvmMethod { name, params, ret }


@JvmMethod(name="jSum", params="java/lang/Integer, java/lang/Integer, java/lang/Integer, java/lang/Integer", ret="java/lang/Integer")
public fun sumTl(a, b, c, d) {
    var sum = a + b + c + d
    return sum
}

@JvmMethod(name="jAve", params="int, int, int, int", ret="int")
@JvmMethod(name="jAve", params="short, short, short, short", ret="short")
@JvmMethod(name="jAve", params="long, long, long, long", ret="long")
@JvmMethod(name="jAve", params="double, double, double, double", ret="double")
public fun ave(a, b, c, d) {
    return (a + b + c + d) / 4
}

fun testWrapped() {
     var ave = AnnotationTest.jSum(100, 200, 300, 400)
     Assert.assertEquals(100 + 200 + 300 + 400, ave)
}

fun testPrimitive() {
    var ave = AnnotationTest.jAve(100, 200, 300, 400)
    Assert.assertEquals((100 + 200 + 300 + 400) / 4, ave)
}

fun testAnnotatedFunInClass() {
    var obj = AnnotatedClass(10, 10, 10)
    Assert.assertEquals(1000.0, obj.doubleProduct(), 0.001)
}

class AnnotatedClass(x, y, z) {

    @JvmMethod(name="doubleProduct", ret="double")
    public fun product() {
        return x * y * z
    }
}
