package org.raven.core;

import org.junit.Test;
import org.raven.core.wrappers.TBoolean;

import static org.junit.Assert.*;

public class TBooleanTest {

    @Test
    public void isTrueTest() {
        assertTrue(TBoolean.TRUE.isTrue());
        assertTrue(!TBoolean.FALSE.isTrue());
    }

    @Test
    public void notTest() {
        assertTrue(TBoolean.FALSE.not().isTrue());
        assertTrue(!TBoolean.TRUE.not().isTrue());
    }

    @Test
    public void toBooleanTest() {
        assertTrue(TBoolean.TRUE.toBoolean());
        assertTrue(!TBoolean.FALSE.toBoolean());
    }

    @Test
    public void toObjectTest() {
        assertTrue(TBoolean.TRUE.toObject() == Boolean.TRUE);
        assertTrue(TBoolean.FALSE.toObject() == Boolean.FALSE);
    }

    @Test
    public void toStringTest() {
        assertTrue(TBoolean.TRUE.toString().equals("true"));
        assertTrue(TBoolean.FALSE.toString().equals("false"));
    }
}
