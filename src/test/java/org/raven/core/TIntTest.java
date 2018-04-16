package org.raven.core;

import org.junit.Test;
import org.raven.core.wrappers.TInt;
import org.raven.core.wrappers.TObject;
import org.raven.core.wrappers.TReal;
import org.raven.core.wrappers.TString;

import static org.junit.Assert.*;

public class TIntTest {

    @Test
    public void getValueTest() {
        TInt a = new TInt(10000000);
        assertEquals(10000000, a.getValue());
    }

    @Test
    public void notTest() {
        TInt a = new TInt(0);
        assertEquals(a.not(), a);
        a = new TInt(-100);
        assertEquals(a.not(), a.not());
        assertEquals(100, (int) a.not().toInt());
    }

    @Test
    public void isTrueTest() {
        TInt a = new TInt(0);
        assertTrue(!a.isTrue());
        a = new TInt(Integer.MIN_VALUE);
        assertTrue(a.isTrue());
    }

    @Test
    public void addTest() {
        TInt a = new TInt(100);
        TInt b = new TInt(-100);
        TInt c = new TInt(2000000000);
        TString str = new TString(" add a string");

        TString expected = new TString("100 add a string");

        assertEquals(0, (int) a.add(b).toInt());
        assertEquals(2000000100, (int) a.add(c).toInt());

        assertEquals(400, (int) a.add(a).add(a).add(a).toInt());

        // coercion

        assertEquals(a.add(str), expected);
        assertEquals(a.add(new TReal(100.0)), new TReal(200.0));
    }

    @Test
    public void subTest() {
        TInt a = new TInt(100);
        TInt b = new TInt(-100);

        assertEquals(a.sub(b), new TInt(200));
        assertEquals(a.sub(new TReal(100.0)), new TReal(0));

    }

    @Test
    public void mulTest() {
        TInt a = new TInt(10);
        assertEquals(100, (int) a.mul(a).toInt());
        assertEquals(1000, (int) a.mul(a).mul(a).toInt());
    }

    @Test
    public void divTest() {

    }

    @Test
    public void modTest() {
        TInt a = new TInt(2);
        TInt b = new TInt(10);
        TInt c = new TInt(0);
        TInt d = new TInt(21);

        assertEquals(2, (int) a.mod(b).toInt());
        assertEquals(0, (int) c.mod(a).toInt());
        assertEquals(0, (int) c.mod(a).toInt());
        assertEquals(1, (int) d.mod(b).toInt());

        assertEquals((int) b.mod(d.not()).toInt(), (10 % -21));
    }

    @Test
    public void powTest() {
        TInt a = new TInt(2);
        assertEquals(16, (int) a.pow(a).pow(a).toInt());
        assertEquals(0.25, a.pow(a.not()).toFloat(), 0.0);
    }

    @Test
    public void GTTest() {
        TInt a = new TInt(100);
        TInt b = new TInt(99);
        TReal c = new TReal(99.99999999999);
        assertTrue(!a.GT(a).isTrue());
        assertTrue(a.GT(b).isTrue());
        assertTrue(!b.GT(a).isTrue());

        assertTrue(a.GT(c).isTrue());

    }

    @Test
    public void LTTest() {
        TInt a = new TInt(100);
        TInt b = new TInt(99);
        TReal c = new TReal(99.99999999999);
        assertTrue(!a.LT(a).isTrue());
        assertTrue(!a.LT(b).isTrue());
        assertTrue(b.LT(a).isTrue());

        assertTrue(!a.LT(c).isTrue());
    }

    @Test
    public void GTETest() {
        TInt a = new TInt(100);
        TInt b = new TInt(99);
        TReal c = new TReal(100);
        assertTrue(a.GTE(a).isTrue());
        assertTrue(a.GTE(b).isTrue());
        assertTrue(a.GTE(c).isTrue());

        c = new TReal(100.00000000000001);
        assertTrue(!a.GTE(c).isTrue());

    }

    @Test
    public void LTETest() {
        TInt a = new TInt(100);
        TInt b = new TInt(99);
        TReal c = new TReal(100);
        assertTrue(a.LTE(a).isTrue());
        assertTrue(!a.LTE(b).isTrue());
        assertTrue(a.LTE(c).isTrue());

        c = new TReal(100.00000000000001);
        assertTrue(a.LTE(c).isTrue());
    }

    @Test
    public void NETest() {
        TInt a = new TInt(100);
        TInt b = new TInt(1000);
        TReal c = new TReal(100);

        assertTrue(a.NE(b).isTrue());
        assertTrue(!a.NE(a).isTrue());
        assertTrue(b.NE(c).isTrue());
        assertTrue(!a.NE(c).isTrue());
    }

    @Test
    public void toIntTest() {
        assertEquals(100, (int) new TInt(100).toInt());
        assertEquals((int) new TInt(-100).toInt(), -100);
    }

    @Test
    public void toByteTest() {
        assertEquals((byte) new TInt(0xffffff).toByte(), -1);
        assertEquals(100, (byte) new TInt(100).toByte());
    }

    @Test
    public void toShortTest() {
        assertEquals((short) new TInt(0xffffffff).toShort(), -1);
        assertEquals(32000, (short) new TInt(32000).toShort());
    }

    @Test
    public void toLongTest() {
        assertEquals(10000, (long) new TInt(10000).toLong());
        assertEquals((long) new TInt(-10000).toLong(), -10000);
    }

    @Test
    public void toFloatTest() {
        assertEquals(0, new TInt(0).toFloat(), 0.00000001);
        assertEquals(123, new TInt(123).toFloat(), 0.00000001);
    }

    @Test
    public void toDoubleTest() {
        assertEquals(0, new TInt(0).toDouble(), 0.00000001);
        assertEquals(123, new TInt(123).toDouble(), 0.00000001);
    }

    @Test
    public void toBooleanTest() {
        assertNotNull(new TInt(10000).toBoolean());
        assertTrue(new TInt(100).toBoolean());
        assertTrue(!new TInt(0).toBoolean());
    }

    @Test
    public void toObjectTest() {
        assertTrue(new TInt(100).toObject() instanceof Integer);
        assertEquals(-100, new TInt(-100).toObject());
    }

    @Test
    public void toStringTest() {
        assertEquals("100", new TInt(100).toString());
        assertEquals("-100", new TInt(-100).toString());
        assertEquals(new TInt(Integer.MAX_VALUE).toString(), "" + Integer.MAX_VALUE);
    }

    @Test
    public void EQTest() {
        TInt a = new TInt(123);
        TInt b = new TInt(321);
        TInt c = new TInt(0);
        assertTrue(!a.EQ(b).isTrue());
        assertTrue(a.EQ(a).isTrue());
        assertTrue(b.EQ(b).isTrue());
        assertTrue(c.EQ(new TInt(0)).isTrue());

        assertTrue(a.EQ(new TReal(123)).isTrue());
        assertTrue(!a.EQ(new TReal(123.000000001)).isTrue());
    }

    @Test
    public void testIntRating() {
        final TInt anInt = new TInt(50);

        assertEquals(TObject.COERCE_IDEAL, anInt.coerceRating(int.class));
        assertEquals(TObject.COERCE_IDEAL, anInt.coerceRating(Integer.class));

        assertEquals(TObject.COERCE_IDEAL, anInt.coerceRating(long.class));
        assertEquals(TObject.COERCE_IDEAL, anInt.coerceRating(Long.class));

        assertEquals(TObject.COERCE_LESS_IDEAL, anInt.coerceRating(short.class));
        assertEquals(TObject.COERCE_LESS_IDEAL, anInt.coerceRating(Short.class));

        assertEquals(TObject.COERCE_LESS_IDEAL, anInt.coerceRating(Byte.class));
        assertEquals(TObject.COERCE_LESS_IDEAL, anInt.coerceRating(Byte.class));

        assertEquals(TObject.COERCE_BAD, anInt.coerceRating(float.class));
        assertEquals(TObject.COERCE_BAD, anInt.coerceRating(Float.class));

        assertEquals(TObject.COERCE_BAD, anInt.coerceRating(double.class));
        assertEquals(TObject.COERCE_BAD, anInt.coerceRating(Double.class));

        assertEquals(TObject.COERCE_IMPOSSIBLE, anInt.coerceRating(String.class));
        assertEquals(TObject.COERCE_IMPOSSIBLE, anInt.coerceRating(Boolean.class));
        assertEquals(TObject.COERCE_IMPOSSIBLE, anInt.coerceRating(boolean.class));
    }

    @Test
    public void testIntCoercion() {
        final TInt anInt = new TInt(50);

        assertEquals(50, (int) anInt.coerce(int.class));
        assertEquals(50, anInt.coerce(Integer.class));

        assertEquals(50, (long) anInt.coerce(long.class));
        assertEquals(50L, anInt.coerce(Long.class));

        assertEquals((short) 50, (short) anInt.coerce(short.class));
        assertEquals((short) 50, anInt.coerce(Short.class));

        assertEquals((byte) 50, (byte) anInt.coerce(byte.class));
        assertEquals((byte) 50, anInt.coerce(Byte.class));

        assertEquals((float) 50, (float) anInt.coerce(float.class), 0.000000001);
        assertEquals((float) 50, (Float) anInt.coerce(Float.class), 0.000000001);

        assertEquals((double) 50, (double) anInt.coerce(double.class), 0.000000001);
        assertEquals((double) 50, (Double) anInt.coerce(Double.class), 0.000000001);
    }
}