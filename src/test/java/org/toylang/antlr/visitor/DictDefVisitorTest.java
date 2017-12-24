package org.toylang.antlr.visitor;

import org.junit.Test;
import org.toylang.antlr.ast.DictDef;
import org.toylang.antlr.ast.Expression;
import org.toylang.antlr.ast.Literal;
import org.toylang.core.wrappers.TBoolean;
import org.toylang.core.wrappers.TInt;
import org.toylang.core.wrappers.TString;

import static org.toylang.antlr.RuleTester.testStatement;

public class DictDefVisitorTest {

    @Test
    public void testEmptyDictionaryDef() {
        Expression[] keys = {};
        Expression[] values = {};
        DictDef def = new DictDef(keys, values);
        testStatement(DictDefVisitor.INSTANCE, "{};", def);
    }

    @Test
    public void testDictDef() {
        Expression[] keys = {new Literal(new TString("aKey")), new Literal(new TInt(4))};
        Expression[] values = {new Literal(new TString("aValue")), new Literal(TBoolean.TRUE)};
        DictDef def = new DictDef(keys, values);
        testStatement(DictDefVisitor.INSTANCE, "{ \"aKey\" : \"aValue\", 4 : true };", def);
    }
}
