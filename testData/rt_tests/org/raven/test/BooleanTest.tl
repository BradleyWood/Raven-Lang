package org.raven.test

import org.junit.Assert

fun boolTest1() {
    Assert.assertEquals(true, true)
}

fun boolTest2() {
    Assert.assertEquals(false, false)
}

fun boolTest3() {
    Assert.assertNotEquals(true, false)
    Assert.assertNotEquals(false, true)
}

fun boolNotTest() {
    Assert.assertEquals(!true, false)
    Assert.assertEquals(!false, true)
    Assert.assertEquals(!!true, true)
    Assert.assertEquals(!!false, false)
}
