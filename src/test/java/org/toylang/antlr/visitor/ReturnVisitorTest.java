package org.toylang.antlr.visitor;

import org.junit.Test;
import org.toylang.antlr.ast.Literal;
import org.toylang.antlr.ast.QualifiedName;
import org.toylang.antlr.ast.Return;
import org.toylang.core.wrappers.*;

import static org.toylang.antlr.RuleTester.testStatement;

public class ReturnVisitorTest {

    @Test
    public void testReturn() {
        Return ret = new Return(null);

        testStatement(ReturnVisitor.INSTANCE, "return;", ret);
    }

    @Test
    public void testReturnVar() {
        Return ret = new Return(new QualifiedName("someVar"));

        testStatement(ReturnVisitor.INSTANCE, "return someVar;", ret);
    }

    @Test
    public void testReturnLiteral() {
        Return ret = new Return(new Literal(TBoolean.TRUE));
        testStatement(ReturnVisitor.INSTANCE, "return true;", ret);

        ret = new Return(new Literal(new TString("str")));
        testStatement(ReturnVisitor.INSTANCE, "return \"str\";", ret);

        ret = new Return(new Literal(new TInt(5)));
        testStatement(ReturnVisitor.INSTANCE, "return 5;", ret);

        ret = new Return(new Literal(new TReal(0.0)));
        testStatement(ReturnVisitor.INSTANCE, "return 0.0;", ret);

        ret = new Return(new Literal(TNull.NULL));
        testStatement(ReturnVisitor.INSTANCE, "return null;", ret);
    }
}
