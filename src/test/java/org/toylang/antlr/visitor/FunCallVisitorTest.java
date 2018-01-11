package org.toylang.antlr.visitor;

import org.junit.Test;
import org.toylang.antlr.ast.Call;
import org.toylang.antlr.ast.Literal;
import org.toylang.antlr.ast.QualifiedName;
import org.toylang.core.wrappers.TBoolean;
import org.toylang.core.wrappers.TInt;

import static org.toylang.antlr.RuleTester.testStatement;

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
