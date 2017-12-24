package org.toylang.antlr.visitor;

import org.junit.Test;
import org.toylang.antlr.ast.Call;
import org.toylang.antlr.ast.Expression;
import org.toylang.antlr.ast.Literal;
import org.toylang.antlr.ast.QualifiedName;
import org.toylang.core.wrappers.TInt;


import static org.toylang.antlr.RuleTester.testStatement;

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
