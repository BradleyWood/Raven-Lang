package org.raven;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.raven.repl.InteractiveInterpreter;

import java.util.function.Function;

public class InteractiveInterpreterTest {

    private Function<Object, Integer> fun;

    @Before
    public void init() throws Throwable {
        final InteractiveInterpreter interpreter = new InteractiveInterpreter();
        interpreter.exec("f(x) = x*x + 100;");

        fun = interpreter.getFunction("f", Integer.class);
    }

    @Test
    public void testGetFunction() {
        Assert.assertEquals(104, (int) fun.apply(2));
        Assert.assertEquals(104, (int) fun.apply(new Object[]{2}));
    }

    @Test(expected = Throwable.class)
    public void testFailureOnIllegalParams() {
        fun.apply("invalid argument");
    }
}
