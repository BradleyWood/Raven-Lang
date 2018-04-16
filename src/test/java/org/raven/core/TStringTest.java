package org.raven.core;


import org.junit.Test;
import org.raven.core.wrappers.*;

import static org.junit.Assert.*;


public class TStringTest {

    @Test
    public void addTest() {
        TString a = new TString("astring");
        TString b = new TString("bstring");
        TString aPlusB = new TString("astringbstring");

        TInt toyInt = new TInt(-100);
        TString bPlusInt = new TString("bstring-100");

        TReal real = new TReal(-100.4201);
        TString aPlusReal = new TString("astring-100.4201");

        TBoolean bool = TBoolean.FALSE;
        TString bPlusBool = new TString("bstringfalse");

        TNull toyNull = TNull.NULL;
        TString aPlusNull = new TString("astringnull");

        TObject obj = new TObject(new Object());
        TString bPlusObj = new TString("bstring" + obj.toString());


        assertEquals(aPlusB, a.add(b));
        assertEquals(bPlusInt, b.add(toyInt));
        assertEquals(aPlusReal, a.add(real));
        assertEquals(bPlusBool, b.add(bool));
        assertEquals(aPlusNull, a.add(toyNull));
        assertEquals(bPlusObj, b.add(obj));

    }

    @Test
    public void EQTest() {
        TString a = new TString("astring");
        TString b = new TString("bstring");
        TNull c = TNull.NULL;
        TString astr2 = new TString("astring");

        assertTrue(a.EQ(a).isTrue());
        assertTrue(a.EQ(astr2).isTrue());
        assertTrue(!a.EQ(b).isTrue());
        assertTrue(!a.EQ(c).isTrue());
    }

    @Test
    public void NETest() {
        TString a = new TString("astring");
        TString b = new TString("bstring");
        TNull c = TNull.NULL;
        TString astr2 = new TString("astring");

        assertTrue(!a.NE(a).isTrue());
        assertTrue(!a.NE(astr2).isTrue());
        assertTrue(a.NE(b).isTrue());
        assertTrue(a.NE(c).isTrue());
    }

    @Test
    public void toObjectTest() {
        TString a = new TString("");
        TString b = new TString("abc");

        assertEquals("", a.toObject());
        assertEquals("abc", b.toObject());
    }

    @Test
    public void toStringTest() {
        TString a = new TString("");
        TString b = new TString("abc");

        assertEquals("", a.toString());
        assertEquals("abc", b.toString());
    }

    @Test
    public void sizeTest() {
        TString a = new TString("");
        TString b = new TString("abc");

        assertEquals(0, a.size());
        assertEquals(3, b.size());
    }
}
