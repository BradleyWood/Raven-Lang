package org.toylang.core;

import org.junit.Test;
import org.toylang.core.wrappers.TInt;
import org.toylang.core.wrappers.TReal;
import org.toylang.core.wrappers.TString;

import static org.junit.Assert.*;

public class ToyIntTest {

    @Test
    public void getValueTest() {
        TInt a = new TInt(10000000);
        assertTrue(a.getValue() == 10000000);
    }

    @Test
    public void notTest() {
        TInt a = new TInt(0);
        assertTrue(a.not().equals(a));
        a = new TInt(-100);
        assertTrue(a.not().equals(a.not()));
        assertTrue(a.not().toInt() == 100);
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

        assertTrue(a.add(b).toInt() == 0);
        assertTrue(a.add(c).toInt() == 2000000100);

        assertTrue(a.add(a).add(a).add(a).toInt() == 400);

        // coercion

        assertTrue(a.add(str).equals(expected));
        assertTrue(a.add(new TReal(100.0)).equals(new TReal(200.0)));
    }

    @Test
    public void subTest() {
        TInt a = new TInt(100);
        TInt b = new TInt(-100);

        assertTrue(a.sub(b).equals(new TInt(200)));
        assertTrue(a.sub(new TReal(100.0)).equals(new TReal(0)));

    }

    @Test
    public void mulTest() {
        TInt a = new TInt(10);
        assertTrue(a.mul(a).toInt() == 100);
        assertTrue(a.mul(a).mul(a).toInt() == 1000);
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

        assertTrue(a.mod(b).toInt() == 2);
        assertTrue(c.mod(a).toInt() == 0);
        assertTrue(c.mod(a).toInt() == 0);
        assertTrue(d.mod(b).toInt() == 1);

        assertTrue(b.mod(d.not()).toInt() == (10 % -21));
    }

    @Test
    public void powTest() {
        TInt a = new TInt(2);
        assertTrue(a.pow(a).pow(a).toInt() == 16);
        assertTrue(a.pow(a.not()).toFloat() == 0.25);
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
        assertTrue(new TInt(100).toInt() == 100);
        assertTrue(new TInt(-100).toInt() == -100);
    }

    @Test
    public void toByteTest() {
        assertTrue(new TInt(0xffffff).toByte() == -1);
        assertTrue(new TInt(100).toByte() == 100);
    }

    @Test
    public void toShortTest() {
        assertTrue(new TInt(0xffffffff).toShort() == -1);
        assertTrue(new TInt(32000).toShort() == 32000);
    }

    @Test
    public void toLongTest() {
        assertTrue(new TInt(10000).toLong() == 10000);
        assertTrue(new TInt(-10000).toLong() == -10000);
    }

    @Test
    public void toFloatTest() {
        assertTrue(new TInt(0).toFloat() == 0);
        assertTrue(new TInt(123).toFloat() == 123);
    }

    @Test
    public void toDoubleTest() {
        assertTrue(new TInt(0).toDouble() == 0);
        assertTrue(new TInt(123).toDouble() == 123);
    }

    @Test
    public void toBooleanTest() {
        assertTrue(new TInt(10000).toBoolean() != null);
        assertTrue(new TInt(100).toBoolean());
        assertTrue(!new TInt(0).toBoolean());
    }

    @Test
    public void toObjectTest() {
        assertTrue(new TInt(100).toObject() instanceof Integer);
        assertTrue(new TInt(-100).toObject().equals(-100));
    }

    @Test
    public void toStringTest() {
        assertTrue(new TInt(100).toString().equals("100"));
        assertTrue(new TInt(-100).toString().equals("-100"));
        assertTrue(new TInt(Integer.MAX_VALUE).toString().equals("" + Integer.MAX_VALUE));
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
}