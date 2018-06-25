package org.raven.antlr.visitor;

import org.junit.Test;
import org.raven.antlr.ast.Block;
import org.raven.antlr.ast.Literal;
import org.raven.antlr.ast.QualifiedName;
import org.raven.antlr.ast.VarDecl;
import org.raven.core.wrappers.TInt;

import static org.raven.antlr.RuleTester.testStatement;

public class BlockVisitorTest {

    @Test
    public void testEmptyBlock() {
        final Block empty = new Block();

        testStatement(BlockVisitor.INSTANCE, "{}", empty);
    }

    @Test
    public void testBlock() {
        final Block block = new Block();
        block.append(new VarDecl(new QualifiedName("a"), new Literal(new TInt(0))));
        block.append(new VarDecl(new QualifiedName("b"), new Literal(new TInt(10))));
        block.append(new VarDecl(new QualifiedName("c"), new Literal(new TInt(20))));

        testStatement(BlockVisitor.INSTANCE, "{ var a = 0\r\nvar b = 10; var c = 20 }", block);
    }
}
