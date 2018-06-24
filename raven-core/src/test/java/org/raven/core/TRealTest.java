package org.raven.core;

import org.junit.Assert;
import org.junit.Test;
import org.raven.core.wrappers.TInt;
import org.raven.core.wrappers.TReal;

public class TRealTest {

    @Test
    public void getValueTest() {
        TReal real = new TReal(0.0);
        Assert.assertEquals(0.0, real.getValue(), 0.00000001);
        real = new TReal(1e6);
        Assert.assertEquals(1e6, real.getValue(), 0.00000001);
    }

    @Test
    public void isTrueTest() {
        TReal real = new TReal(0.0);
        Assert.assertFalse(real.isTrue());
        real = new TReal(0.00000001);
        Assert.assertTrue(real.isTrue());
        real = new TReal(-0.00000001);
        Assert.assertTrue(real.isTrue());
    }

    @Test
    public void notTest() {
        TReal a = new TReal(1.0);
        TReal b = new TReal(-1.0);
        Assert.assertEquals(b, a.not());
        Assert.assertEquals(a, b.not());
        Assert.assertEquals(0.0, a.add(a.not()).toFloat(), 0.0000000001);
    }

    @Test
    public void addTest() {
        TReal a = new TReal(1.0);
        TReal expected = new TReal(2.0);
        Assert.assertEquals(expected, a.add(a));
        Assert.assertEquals(expected, a.add(new TInt(1)));
    }

    @Test
    public void subTest() {
        TReal a = new TReal(1.0);
        TReal expected = new TReal(0);
        Assert.assertEquals(expected, a.sub(a));
        Assert.assertEquals(expected, a.sub(new TInt(1)));
    }

    @Test
    public void mulTest() {

    }

    @Test
    public void divTest() {
    }

    @Test
    public void modTest() {
    }

    @Test
    public void powTest() {
    }

    @Test
    public void GTTest() {
    }

    @Test
    public void LTTest() {
    }

    @Test
    public void GTETest() {
    }

    @Test
    public void LTETest() {
    }

    @Test
    public void EQTest() {
    }

    @Test
    public void NETest() {
    }

    @Test
    public void toIntTest() {
    }

    @Test
    public void toByteTest() {
    }

    @Test
    public void toShortTest() {
    }

    @Test
    public void toLongTest() {
    }

    @Test
    public void toFloatTest() {
    }

    @Test
    public void toDoubleTest() {
    }

    @Test
    public void toBooleanTest() {
    }

    @Test
    public void toObjectTest() {
    }

    @Test
    public void toStringTest() {

    }
}
