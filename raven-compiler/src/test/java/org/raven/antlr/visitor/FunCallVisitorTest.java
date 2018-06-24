package org.raven.antlr.visitor;

import org.junit.Test;
import org.raven.antlr.ast.Call;
import org.raven.antlr.ast.Literal;
import org.raven.antlr.ast.QualifiedName;
import org.raven.core.wrappers.TBoolean;
import org.raven.core.wrappers.TInt;

import static org.raven.antlr.RuleTester.testStatement;

public class FunCallVisitorTest {

    @Test
    public void testLocalFunctionCall() {
        Call call = new Call(new QualifiedName("aMethod"));
        testStatement(FunCallVisitor.INSTANCE, "aMethod();", call);
    }

    @Test
    public void testLocalFunctionWithParams() {
        Call call = new Call(new QualifiedName("aMethod"),
                new Literal(new TInt(5)), new Literal(TBoolean.TRUE));
        testStatement(FunCallVisitor.INSTANCE, "aMethod(5, true);", call);
    }

}
