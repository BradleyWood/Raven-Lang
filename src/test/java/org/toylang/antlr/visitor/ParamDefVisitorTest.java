package org.toylang.antlr.visitor;

import org.junit.Test;
import org.toylang.antlr.ast.QualifiedName;
import org.toylang.antlr.ast.VarDecl;

import static org.toylang.antlr.RuleTester.testStatement;

public class ParamDefVisitorTest {

    @Test
    public void paramDefTest() {
        VarDecl def = new VarDecl(new QualifiedName("someVar"), null);

        testStatement(ParamDefVisitor.INSTANCE, "someVar", def);
    }

}
