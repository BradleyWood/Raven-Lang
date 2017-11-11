package org.toylang.test;

import org.toylang.core.wrappers.TObject;

import java.util.LinkedList;
import java.util.Objects;

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

    public static void assertEqual(TObject a, TObject b) {
        if (!Objects.equals(a, b)) {
            fail(a + " is not equal to " + b);
        }
    }

    public static void assertNotEqual(TObject a, TObject b) {
        if (Objects.equals(a, b)) {
            fail(a + " is equal to " + b);
        }
    }

    public static void assertEqual(Object a, Object b) {
        if (!Objects.equals(a, b)) {
            fail(a + " is not equal to " + b);
        }
    }

    public static void assertNotEqual(Object a, Object b) {
        if (Objects.equals(a, b)) {
            fail(a + " is equal to " + b);
        }
    }
}
