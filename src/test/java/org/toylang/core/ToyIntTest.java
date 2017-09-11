package org.toylang.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class ToyIntTest {

    @Test
    public void getValueTest() {
        ToyInt a = new ToyInt(10000000);
        assertTrue(a.getValue() == 10000000);
    }

    @Test
    public void notTest() {
        ToyInt a = new ToyInt(0);
        assertTrue(a.not().equals(a));
        a = new ToyInt(-100);
        assertTrue(a.not().equals(a.not()));
        assertTrue(a.not().toInt() == 100);
    }

    @Test
    public void isTrueTest() {
        ToyInt a = new ToyInt(0);
        assertTrue(!a.isTrue());
        a = new ToyInt(Integer.MIN_VALUE);
        assertTrue(a.isTrue());
    }

    @Test
    public void addTest() {
        ToyInt a = new ToyInt(100);
        ToyInt b = new ToyInt(-100);
        ToyInt c = new ToyInt(2000000000);
        ToyString str = new ToyString(" add a string");

        ToyString expected = new ToyString("100 add a string");

        assertTrue(a.add(b).toInt() == 0);
        assertTrue(a.add(c).toInt() == 2000000100);

        assertTrue(a.add(a).add(a).add(a).toInt() == 400);

        // coercion

        assertTrue(a.add(str).equals(expected));
        assertTrue(a.add(new ToyReal(100.0)).equals(new ToyReal(200.0)));
    }

    @Test
    public void subTest() {
        ToyInt a = new ToyInt(100);
        ToyInt b = new ToyInt(-100);

        assertTrue(a.sub(b).equals(new ToyInt(200)));
        assertTrue(a.sub(new ToyReal(100.0)).equals(new ToyReal(0)));

    }

    @Test
    public void mulTest() {
        ToyInt a = new ToyInt(10);
        assertTrue(a.mul(a).toInt() == 100);
        assertTrue(a.mul(a).mul(a).toInt() == 1000);
    }

    @Test
    public void divTest() {

    }

    @Test
    public void modTest() {
        ToyInt a = new ToyInt(2);
        ToyInt b = new ToyInt(10);
        ToyInt c = new ToyInt(0);
        ToyInt d = new ToyInt(21);

        assertTrue(a.mod(b).toInt() == 2);
        assertTrue(c.mod(a).toInt() == 0);
        assertTrue(c.mod(a).toInt() == 0);
        assertTrue(d.mod(b).toInt() == 1);

        assertTrue(b.mod(d.not()).toInt() == (10 % -21));
    }

    @Test
    public void powTest() {
        ToyInt a = new ToyInt(2);
        assertTrue(a.pow(a).pow(a).toInt() == 16);
        assertTrue(a.pow(a.not()).toFloat() == 0.25);
    }

    @Test
    public void GTTest() {
        ToyInt a = new ToyInt(100);
        ToyInt b = new ToyInt(99);
        ToyReal c = new ToyReal(99.99999999999);
        assertTrue(!a.GT(a).isTrue());
        assertTrue(a.GT(b).isTrue());
        assertTrue(!b.GT(a).isTrue());

        assertTrue(a.GT(c).isTrue());

    }

    @Test
    public void LTTest() {
        ToyInt a = new ToyInt(100);
        ToyInt b = new ToyInt(99);
        ToyReal c = new ToyReal(99.99999999999);
        assertTrue(!a.LT(a).isTrue());
        assertTrue(!a.LT(b).isTrue());
        assertTrue(b.LT(a).isTrue());

        assertTrue(!a.LT(c).isTrue());
    }

    @Test
    public void GTETest() {
        ToyInt a = new ToyInt(100);
        ToyInt b = new ToyInt(99);
        ToyReal c = new ToyReal(100);
        assertTrue(a.GTE(a).isTrue());
        assertTrue(a.GTE(b).isTrue());
        assertTrue(a.GTE(c).isTrue());

        c = new ToyReal(100.00000000000001);
        assertTrue(!a.GTE(c).isTrue());

    }

    @Test
    public void LTETest() {
        ToyInt a = new ToyInt(100);
        ToyInt b = new ToyInt(99);
        ToyReal c = new ToyReal(100);
        assertTrue(a.LTE(a).isTrue());
        assertTrue(!a.LTE(b).isTrue());
        assertTrue(a.LTE(c).isTrue());

        c = new ToyReal(100.00000000000001);
        assertTrue(a.LTE(c).isTrue());
    }

    @Test
    public void NETest() {
        ToyInt a = new ToyInt(100);
        ToyInt b = new ToyInt(1000);
        ToyReal c = new ToyReal(100);

        assertTrue(a.NE(b).isTrue());
        assertTrue(!a.NE(a).isTrue());
        assertTrue(b.NE(c).isTrue());
        assertTrue(!a.NE(c).isTrue());
    }

    @Test
    public void toIntTest() {
        assertTrue(new ToyInt(100).toInt() == 100);
        assertTrue(new ToyInt(-100).toInt() == -100);
    }

    @Test
    public void toByteTest() {
        assertTrue(new ToyInt(0xffffff).toByte() == -1);
        assertTrue(new ToyInt(100).toByte() == 100);
    }

    @Test
    public void toShortTest() {
        assertTrue(new ToyInt(0xffffffff).toShort() == -1);
        assertTrue(new ToyInt(32000).toShort() == 32000);
    }

    @Test
    public void toLongTest() {
        assertTrue(new ToyInt(10000).toLong() == 10000);
        assertTrue(new ToyInt(-10000).toLong() == -10000);
    }

    @Test
    public void toFloatTest() {
        assertTrue(new ToyInt(0).toFloat() == 0);
        assertTrue(new ToyInt(123).toFloat() == 123);
    }

    @Test
    public void toDoubleTest() {
        assertTrue(new ToyInt(0).toDouble() == 0);
        assertTrue(new ToyInt(123).toDouble() == 123);
    }

    @Test
    public void toBooleanTest() {
        assertTrue(new ToyInt(10000).toBoolean() != null);
        assertTrue(new ToyInt(100).toBoolean());
        assertTrue(!new ToyInt(0).toBoolean());
    }

    @Test
    public void toObjectTest() {
        assertTrue(new ToyInt(100).toObject() instanceof Integer);
        assertTrue(new ToyInt(-100).toObject().equals(-100));
    }

    @Test
    public void toStringTest() {
        assertTrue(new ToyInt(100).toString().equals("100"));
        assertTrue(new ToyInt(-100).toString().equals("-100"));
        assertTrue(new ToyInt(Integer.MAX_VALUE).toString().equals("" + Integer.MAX_VALUE));
    }

    @Test
    public void EQTest() {
        ToyInt a = new ToyInt(123);
        ToyInt b = new ToyInt(321);
        ToyInt c = new ToyInt(0);
        assertTrue(!a.EQ(b).isTrue());
        assertTrue(a.EQ(a).isTrue());
        assertTrue(b.EQ(b).isTrue());
        assertTrue(c.EQ(new ToyInt(0)).isTrue());

        assertTrue(a.EQ(new ToyReal(123)).isTrue());
        assertTrue(!a.EQ(new ToyReal(123.000000001)).isTrue());
    }
}