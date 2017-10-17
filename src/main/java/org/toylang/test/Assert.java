package org.toylang.test;

import java.util.LinkedList;

public class Assert {

    public static LinkedList<AssertionError> errors = new LinkedList<>();

    public static void fail() {
        fail("");
    }

    public static void fail(String msg) {
        errors.add(new AssertionError(msg));
    }

    public static void assertTrue(boolean b) {
        if (!b)
            fail("value is not true");
    }

    public static void assertEqual(Object a, Object b) {
        if (!a.equals(b)) {
            fail(a + " is not equal to " + b);
        }
    }

    public static void assertNotEqual(Object a, Object b) {
        if (a.equals(b)) {
            fail(a + " is equal to " + b);
        }
    }
}
