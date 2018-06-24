package org.raven.antlr.visitor;

import org.junit.Test;
import org.raven.antlr.Operator;
import org.raven.antlr.ast.BinOp;
import org.raven.antlr.ast.ListDef;
import org.raven.antlr.ast.Literal;
import org.raven.antlr.ast.QualifiedName;
import org.raven.core.wrappers.TBoolean;
import org.raven.core.wrappers.TInt;
import org.raven.core.wrappers.TReal;
import org.raven.core.wrappers.TString;

import static org.raven.antlr.RuleTester.testStatement;

public class ArrayDefVisitorTest {

    @Test
    public void testEmptyListef() {
        ListDef def = new ListDef();
        testStatement(ArrayDefVisitor.INSTANCE, "[];", def);
    }

    @Test
    public void testNumberedListDef() {
        ListDef def = new ListDef(new Literal(new TInt(4)), new Literal(new TInt(5)),
                new Literal(new TInt(6)), new Literal(new TInt(7)));
        testStatement(ArrayDefVisitor.INSTANCE, "[4, 5, 6, 7];", def);
    }

    @Test
    public void testBooleanList() {
        ListDef def = new ListDef(new Literal(TBoolean.TRUE), new Literal(TBoolean.FALSE));
        testStatement(ArrayDefVisitor.INSTANCE, "[true, false];", def);
    }

    @Test
    public void testRealList() {
        ListDef def = new ListDef(new Literal(new TReal(5.5)), new Literal(new TReal(0.0)));
        testStatement(ArrayDefVisitor.INSTANCE, "[5.5, 0.0];", def);
    }

    @Test
    public void testListWithQNames() {
        ListDef def = new ListDef(new QualifiedName("someObj"), new QualifiedName("anotherObj"));
        testStatement(ArrayDefVisitor.INSTANCE, "[someObj, anotherObj];", def);
    }

    @Test
    public void testAssortedList() {
        ListDef def = new ListDef(new QualifiedName("someObj"), new Literal(TBoolean.TRUE),
                new Literal(new TInt(5)), new Literal(new TString("abcdefg")),
                new BinOp(new Literal(new TInt(5)), Operator.MULT, new QualifiedName("gg")));
        testStatement(ArrayDefVisitor.INSTANCE, "[someObj, true, 5, \"abcdefg\", 5 * gg];", def);
    }
}
