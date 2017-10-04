package org.toylang.core;


import org.junit.Test;

import static org.junit.Assert.*;


public class ToyStringTest {

    @Test
    public void addTest() {
        ToyString a = new ToyString("astring");
        ToyString b = new ToyString("bstring");
        ToyString aPlusB = new ToyString("astringbstring");

        ToyInt toyInt = new ToyInt(-100);
        ToyString bPlusInt = new ToyString("bstring-100");

        ToyReal real = new ToyReal(-100.4201);
        ToyString aPlusReal = new ToyString("astring-100.4201");

        ToyBoolean bool = new ToyBoolean(false);
        ToyString bPlusBool = new ToyString("bstringfalse");

        ToyNull toyNull = new ToyNull();
        ToyString aPlusNull = new ToyString("astringnull");

        ToyObject obj = new ToyObject(new Object());
        ToyString bPlusObj = new ToyString("bstring" + obj.toString());


        assertTrue(a.add(b).equals(aPlusB));
        assertTrue(b.add(toyInt).equals(bPlusInt));
        assertTrue(a.add(real).equals(aPlusReal));
        assertTrue(b.add(bool).equals(bPlusBool));
        assertTrue(a.add(toyNull).equals(aPlusNull));
        assertTrue(b.add(obj).equals(bPlusObj));

    }

    @Test
    public void EQTest() {
        ToyString a = new ToyString("astring");
        ToyString b = new ToyString("bstring");
        ToyNull c = new ToyNull();
        ToyString astr2 = new ToyString("astring");

        assertTrue(a.EQ(a).isTrue());
        assertTrue(a.EQ(astr2).isTrue());
        assertTrue(!a.EQ(b).isTrue());
        assertTrue(!a.EQ(c).isTrue());
    }

    @Test
    public void NETest() {
        ToyString a = new ToyString("astring");
        ToyString b = new ToyString("bstring");
        ToyNull c = new ToyNull();
        ToyString astr2 = new ToyString("astring");

        assertTrue(!a.NE(a).isTrue());
        assertTrue(!a.NE(astr2).isTrue());
        assertTrue(a.NE(b).isTrue());
        assertTrue(a.NE(c).isTrue());
    }

    @Test
    public void toObjectTest() {
        ToyString a = new ToyString("");
        ToyString b = new ToyString("abc");

        assertTrue(a.toObject().equals(""));
        assertTrue(b.toObject().equals("abc"));
    }

    @Test
    public void toStringTest() {
        ToyString a = new ToyString("");
        ToyString b = new ToyString("abc");

        assertTrue(a.toString().equals(""));
        assertTrue(b.toString().equals("abc"));
    }

    @Test
    public void sizeTest() {
        ToyString a = new ToyString("");
        ToyString b = new ToyString("abc");

        assertTrue(a.size() == 0);
        assertTrue(b.size() == 3);
    }
}
