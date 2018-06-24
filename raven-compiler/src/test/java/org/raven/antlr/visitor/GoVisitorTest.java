package org.raven.antlr.visitor;


import org.junit.Test;
import org.raven.antlr.ast.Call;
import org.raven.antlr.ast.Go;
import org.raven.antlr.ast.Literal;
import org.raven.antlr.ast.QualifiedName;
import org.raven.core.wrappers.TBoolean;
import org.raven.core.wrappers.TInt;
import org.raven.core.wrappers.TString;

import static org.raven.antlr.RuleTester.testStatement;

public class GoVisitorTest {

    @Test
    public void testGoVisitor() {
        Go goCall = new Go(new Call(new QualifiedName("toHell")));
        testStatement(GoVisitor.INSTANCE, "go toHell();", goCall);
    }

    @Test
    public void testGoWithParams() {
        Go goCall = new Go(new Call(new QualifiedName("toHell"), new Literal(new TString("test"))));
        testStatement(GoVisitor.INSTANCE, "go toHell(\"test\");", goCall);
    }

    @Test
    public void testGoWithParams2() {
        Go goCall = new Go(new Call(new QualifiedName("toHell"), new Literal(new TInt(5)),
                new Literal(TBoolean.TRUE), new Literal(new TString("test"))));
        testStatement(GoVisitor.INSTANCE, "go toHell(5, true, \"test\");", goCall);
    }

    @Test
    public void testGoOnObj() {
        Call call = new Call(new QualifiedName("toHell"));
        call.setPrecedingExpr(new QualifiedName("obj"));
        Go goCall = new Go(call);

        testStatement(GoVisitor.INSTANCE, "go obj.toHell();", goCall);
    }

}
