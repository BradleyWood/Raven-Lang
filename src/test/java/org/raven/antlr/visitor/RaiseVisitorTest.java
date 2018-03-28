package org.raven.antlr.visitor;

import org.junit.Test;
import org.raven.antlr.ast.Call;
import org.raven.antlr.ast.QualifiedName;
import org.raven.antlr.ast.Raise;

import static org.raven.antlr.RuleTester.testStatement;

public class RaiseVisitorTest {

    @Test
    public void raiseTestQn() {
        Raise expected = new Raise(new QualifiedName("hell"));
        testStatement(RaiseVisitor.INSTANCE, "raise hell;", expected);
    }

    @Test
    public void raiseTestFun() {
        Raise expected = new Raise(new Call(new QualifiedName("hell")));
        testStatement(RaiseVisitor.INSTANCE, "raise hell();", expected);
    }
}
