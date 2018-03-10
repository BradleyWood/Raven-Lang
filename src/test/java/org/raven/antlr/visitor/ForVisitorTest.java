package org.raven.antlr.visitor;

import org.junit.Test;
import org.raven.antlr.Operator;
import org.raven.antlr.ast.*;
import org.raven.core.wrappers.TInt;

import static org.raven.antlr.RuleTester.testStatement;

public class ForVisitorTest {

    @Test
    public void forRangeTest() {
        VarDecl init = new VarDecl(new QualifiedName("i"), new Literal(new TInt(0)));
        BinOp condition = new BinOp(new QualifiedName("i"), Operator.LT, new Literal(new TInt(10)));
        BinOp after = new BinOp(new QualifiedName("i"), Operator.ASSIGNMENT,
                new BinOp(new QualifiedName("i"), Operator.ADD, new Literal(new TInt(1))));
        Statement body = new Block();

        For forLoop = new For(init, condition, new Block(body), new Block(after));

        testStatement(ForVisitor.INSTANCE, "for i range 0 to 10 {}", forLoop);
    }
}
