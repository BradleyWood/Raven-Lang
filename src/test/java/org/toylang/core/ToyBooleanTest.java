package org.toylang.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class ToyBooleanTest {

    @Test
    public void isTrueTest() {
        assertTrue(ToyBoolean.TRUE.isTrue());
        assertTrue(!ToyBoolean.FALSE.isTrue());
    }

    @Test
    public void notTest() {
        assertTrue(ToyBoolean.FALSE.not().isTrue());
        assertTrue(!ToyBoolean.TRUE.not().isTrue());
    }

    @Test
    public void toBooleanTest() {
        assertTrue(ToyBoolean.TRUE.toBoolean());
        assertTrue(!ToyBoolean.FALSE.toBoolean());
    }

    @Test
    public void toObjectTest() {
        assertTrue(ToyBoolean.TRUE.toObject() == Boolean.TRUE);
        assertTrue(ToyBoolean.FALSE.toObject() == Boolean.FALSE);
    }

    @Test
    public void toStringTest() {
        assertTrue(ToyBoolean.TRUE.toString().equals("true"));
        assertTrue(ToyBoolean.FALSE.toString().equals("false"));
    }
}
