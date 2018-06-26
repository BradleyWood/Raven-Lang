package org.raven.core;

import org.junit.Assert;
import org.junit.Test;
import org.raven.core.wrappers.TInt;
import org.raven.core.wrappers.TList;
import org.raven.core.wrappers.TString;

public class TListTest {

    @Test
    public void testAdd() {
        final TList lst = new TList();
        lst.add(new TInt(10));
        Assert.assertTrue(lst.contains(new TInt(10)));

        lst.add(110);
        Assert.assertTrue(lst.contains(new TInt(110)));

        lst.add("string");
        Assert.assertTrue(lst.contains(new TString("string")));
    }

}
