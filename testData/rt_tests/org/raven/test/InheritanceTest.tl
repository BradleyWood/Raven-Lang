package org.raven.test;

import org.junit.Assert;

import testclasses.Person;

class SpecialPerson(a, b) extends Person(a, b) {
}

fun testInheritedMethods() {
    // test super() selection
    var me = SpecialPerson("Brad", 420);
    Assert.assertEquals("Brad", me.getName());
    Assert.assertEquals(420, me.getAge());

    me = SpecialPerson(420, "Brad");

    Assert.assertEquals("Brad", me.getName());
    Assert.assertEquals(420, me.getAge());
}
