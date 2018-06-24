package org.raven.core;

import org.junit.Test;
import org.raven.core.wrappers.*;

public class IntrinsicsTest {

    @Test(expected = NullPointerException.class)
    public void testRequireNonNull() {
        Intrinsics.requireNonNull(TNull.NULL);
    }

    @Test(expected = NullPointerException.class)
    public void testRequireNonNull2() {
        Intrinsics.requireNonNull(null);
    }

    @Test
    public void testRequireNonNull3() {
        Intrinsics.requireNonNull(new TList());
        Intrinsics.requireNonNull(new TInt(5));
        Intrinsics.requireNonNull(new TBigInt("51241444245651"));
        Intrinsics.requireNonNull(new TDict());
        Intrinsics.requireNonNull(new TReal(0.0));
        Intrinsics.requireNonNull(new TString(""));
        Intrinsics.requireNonNull(TBoolean.TRUE);
        Intrinsics.requireNonNull(TBoolean.FALSE);
    }

    @Test(expected = RuntimeException.class)
    public void testRequireType() {
        Intrinsics.requireType(TNull.NULL, TString.TYPE, "");
    }

    @Test(expected = RuntimeException.class)
    public void testRequireType2() {
        Intrinsics.requireType(new TInt(5), TList.TYPE, "");
    }

    @Test
    public void testRequireType3() {
        Intrinsics.requireType(new TInt(5), TInt.TYPE, "");
        Intrinsics.requireType(new TReal(0.0), TReal.TYPE, "");

        Intrinsics.requireType(TNull.NULL, TNull.TYPE, "");
        Intrinsics.requireType(TBoolean.TRUE, TBoolean.TYPE, "");
        Intrinsics.requireType(new TString(""), TString.TYPE, "");
    }
}
