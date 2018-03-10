package org.raven.antlr.visitor;

import org.junit.Test;
import org.raven.antlr.ast.DictDef;
import org.raven.antlr.ast.Expression;
import org.raven.antlr.ast.Literal;
import org.raven.core.wrappers.TBoolean;
import org.raven.core.wrappers.TInt;
import org.raven.core.wrappers.TString;

import static org.raven.antlr.RuleTester.testStatement;

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
