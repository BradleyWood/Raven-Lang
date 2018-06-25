package org.raven.core;

import org.junit.Assert;
import org.junit.Test;
import org.raven.core.wrappers.TDict;
import org.raven.core.wrappers.TInt;
import org.raven.core.wrappers.TObject;
import org.raven.core.wrappers.TString;

import java.util.HashMap;
import java.util.Map;

public class TDictTest {

    @Test
    public void testCoercion() {
        final TObject dict = new TDict();
        dict.put(new TString("a"), new TInt(1));
        dict.put(new TString("b"), new TInt(2));
        dict.put(new TString("c"), new TInt(3));

        final Map expected = new HashMap<>();
        expected.put("a", 1);
        expected.put("b", 2);
        expected.put("c", 3);

        Assert.assertEquals(expected, dict.coerce(Map.class));
    }
}
