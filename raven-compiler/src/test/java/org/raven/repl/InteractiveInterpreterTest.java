package org.raven.repl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.raven.core.wrappers.TInt;
import org.raven.core.wrappers.TString;
import org.raven.core.wrappers.TVoid;

import java.util.function.Function;

public class InteractiveInterpreterTest {

    private Function<Object, Integer> fun;

    @Before
    public void init() throws Throwable {
        final InteractiveInterpreter interpreter = new InteractiveInterpreter();
        interpreter.exec("f(x, y) = x + y;");
        interpreter.exec("f(x) = x * x + 100;");

        fun = interpreter.getFunction("f", Integer.class);
    }

    @Test
    public void testGetFunction() {
        Assert.assertEquals(104, (int) fun.apply(2));
        Assert.assertEquals(104, (int) fun.apply(new Object[]{2}));

        Assert.assertEquals(4, (int) fun.apply(new Object[]{2, 2}));
    }

    @Test(expected = Throwable.class)
    public void testFailureOnIllegalParams() {
        fun.apply("invalid argument");
    }

    @Test
    public void testEval() {
        final InteractiveInterpreter interpreter = new InteractiveInterpreter();
        Assert.assertEquals(new TInt(45), interpreter.eval("5 * 9"));
        Assert.assertEquals(new TString("literal"), interpreter.eval("\"literal\""));

        Assert.assertEquals(TVoid.VOID, interpreter.eval("f(x) = 100"));
        Assert.assertEquals(TVoid.VOID, interpreter.eval("for (i range 0 to 10) {}"));
    }
}
