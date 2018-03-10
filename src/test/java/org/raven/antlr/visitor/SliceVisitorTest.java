package org.raven.antlr.visitor;

import org.junit.Test;
import org.raven.antlr.ast.Call;
import org.raven.antlr.ast.Expression;
import org.raven.antlr.ast.Literal;
import org.raven.antlr.ast.QualifiedName;
import org.raven.core.wrappers.TInt;


import static org.raven.antlr.RuleTester.testStatement;

public class SliceVisitorTest {

    private final QualifiedName SLICE_FUN = new QualifiedName("subList");
    private final Expression start = new Literal(new TInt(0));


    @Test
    public void testFullSlice() {
        QualifiedName varName = new QualifiedName("lst");
        Expression end = new Call((Expression) varName, new QualifiedName("size"));
        Call call = new Call((Expression) varName, SLICE_FUN,
                start, end);
        testStatement(SliceVisitor.INSTANCE, "lst[:];", call);
    }

    @Test
    public void testLowerBoundedSlice() {
        QualifiedName varName = new QualifiedName("lst");
        Expression end = new Call((Expression) varName, new QualifiedName("size"));
        Call call = new Call((Expression) varName, SLICE_FUN,
                new Literal(new TInt(4)), end);
        testStatement(SliceVisitor.INSTANCE, "lst[4:];", call);
    }

    @Test
    public void testUpperBoundedSlice() {
        QualifiedName varName = new QualifiedName("lst");
        Call call = new Call((Expression) varName, SLICE_FUN,
                start, new Literal(new TInt(4)));
        testStatement(SliceVisitor.INSTANCE, "lst[:4];", call);
    }

    @Test
    public void testSliceIntBounds() {
        Call call = new Call((Expression) new QualifiedName("lst"), SLICE_FUN,
                start, new Literal(new TInt(4)));
        testStatement(SliceVisitor.INSTANCE, "lst[0:4];", call);
    }

    @Test
    public void testSliceVarBounds() {
        Call call = new Call((Expression) new QualifiedName("lst"), SLICE_FUN,
                new QualifiedName("a"), new QualifiedName("b"));
        testStatement(SliceVisitor.INSTANCE, "lst[a:b];", call);
    }

}
