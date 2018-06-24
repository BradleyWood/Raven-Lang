package org.raven.test

import org.junit.Assert

import org.raven.core.wrappers.TInt
import org.raven.core.wrappers.TBigInt

fun bigIntMultTest() {
    Assert.assertEquals(100000000000000, 10000000000000 * 10)

    Assert.assertEquals(100000000000000, 10 * 10000000000000)
}

fun bigIntDivTest() {
    Assert.assertEquals(10000000000000, 100000000000000 / 10)
    Assert.assertEquals(0, 10 / 100000000000000)
}

fun bigIntSubTest() {
    Assert.assertEquals(0, 100000000000000 - 100000000000000)
    Assert.assertEquals(100000000000000, 100000000000000 - 0)
}

fun bigIntAddTest() {
    Assert.assertEquals(200000000000000, 100000000000000 + 100000000000000)
    Assert.assertEquals(100000000000000, 100000000000000 + 0)
    Assert.assertEquals(100000000000000, 0 + 100000000000000)
    Assert.assertEquals(100000000000000, 0 + 100000000000000 + 0)
}

fun bigIntPowTest() {
    var bigInt = 10000000000000
    var result = 100000000000000000000000000
    bigInt = bigInt ** 2
    Assert.assertEquals(result, bigInt)

    var a = 0
    a = bigInt ** a
    Assert.assertEquals(a, 1)
}

fun bigIntModTest() {
    Assert.assertEquals(10, 10 % 10000000000000)
    Assert.assertEquals(2, bigInt(10) % 8)
}

fun intAddOverflowTest() {
    var i = 2**30
    var sum = i + i
    Assert.assertEquals(TInt.TYPE, type(i))
    Assert.assertEquals(TBigInt.TYPE, type(sum))
    Assert.assertTrue(sum > i)
}

fun intSubOverflowTest() {
    var i = -(2**30)
    var diff = i - (2**30 + 11)
    Assert.assertEquals(TInt.TYPE, type(i))
    Assert.assertEquals(TBigInt.TYPE, type(diff))
    Assert.assertTrue(diff < i)
}

fun intMultOverflowTest() {
    var i = 2**30
    var product = i * 10
    Assert.assertEquals(TInt.TYPE, type(i))
    Assert.assertEquals(TBigInt.TYPE, type(product))
    Assert.assertTrue(product > i)
    Assert.assertEquals(10737418240, product)
}

fun intPowOverflowTest() {
    Assert.assertEquals(10000000000, 10 ** 10)
}
