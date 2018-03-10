package org.raven.antlr.visitor;

import org.junit.Test;
import org.raven.antlr.ast.QualifiedName;
import org.raven.antlr.ast.VarDecl;

import static org.raven.antlr.RuleTester.testStatement;

public class ParamDefVisitorTest {

    @Test
    public void paramDefTest() {
        VarDecl def = new VarDecl(new QualifiedName("someVar"), null);

        testStatement(ParamDefVisitor.INSTANCE, "someVar", def);
    }

}
