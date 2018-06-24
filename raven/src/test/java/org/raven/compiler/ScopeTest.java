package org.raven.compiler;


import org.junit.Assert;
import org.junit.Test;
import org.raven.error.Errors;

public class ScopeTest {

    private final Scope scope = new Scope();

    @Test
    public void testScopeCreation() {
        Assert.assertEquals(0, scope.count());
        scope.beginScope();
        Assert.assertEquals(1, scope.count());
        scope.endScope();
        Assert.assertEquals(0, scope.count());
        scope.beginScope();
        scope.beginScope();
        scope.beginScope();
        scope.endScope();
        Assert.assertEquals(2, scope.count());
        scope.endScope();
        scope.endScope();
        Assert.assertEquals(0, scope.count());
    }

    @Test(expected = RuntimeException.class)
    public void testScopeUnderflow() {
        scope.beginScope();
        scope.endScope();
        scope.endScope();
    }

    @Test
    public void testVars1() {
        scope.beginScope();

        scope.putVar("test");
        Assert.assertEquals(0, scope.findVar("test"));

        scope.endScope();
    }

    @Test
    public void testVars2() {
        scope.beginScope();

        scope.putVar("test");
        Assert.assertEquals(0, scope.findVar("test"));
        scope.putVar("testing");
        Assert.assertEquals(1, scope.findVar("testing"));

        scope.endScope();
    }

    @Test
    public void testVars3() {
        scope.beginScope();

        scope.putVar("test");
        Assert.assertEquals(0, scope.findVar("test"));

        scope.beginScope();
        scope.putVar("testing");
        Assert.assertEquals(1, scope.findVar("testing"));
        scope.endScope();

        Assert.assertEquals(0, scope.findVar("test"));

        Assert.assertEquals(-1, scope.findVar("testing"));

        scope.endScope();
    }

    @Test
    public void testVarOverload() {
        Errors.reset();
        scope.clear();
        scope.beginScope();

        scope.putVar("test");

        Assert.assertEquals(0, scope.findVar("test"));

        scope.beginScope();

        Assert.assertEquals(0, Errors.getErrorCount());
        scope.putVar("test");
        Assert.assertEquals(1, Errors.getErrorCount());

        scope.endScope();

        scope.endScope();
        scope.clear();
        Errors.reset();
    }
}
