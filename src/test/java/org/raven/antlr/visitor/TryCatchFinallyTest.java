package org.raven.antlr.visitor;

import org.junit.Test;
import org.raven.antlr.ast.Block;
import org.raven.antlr.ast.Call;
import org.raven.antlr.ast.QualifiedName;
import org.raven.antlr.ast.TryCatchFinally;

import static org.raven.antlr.RuleTester.*;

public class TryCatchFinallyTest {

    private final Call call = new Call(new QualifiedName("aMethod"));
    private final Call handlerCall = new Call(new QualifiedName("println"));

    @Test
    public void testTryCatchNoFinally() {
        Block body = new Block(call);
        Block handler = new Block(handlerCall);
        Block finallyBlock = null;

        TryCatchFinally expected = new TryCatchFinally(body, handler, finallyBlock);

        testStatement(TryCatchFinallyVisitor.INSTANCE, "try { aMethod(); } catch { println(); }", expected);
    }

    @Test
    public void testTryCatchFinally() {
        Block body = new Block(call);
        Block handler = new Block(handlerCall);
        Block finallyBlock = new Block(call, handlerCall);

        TryCatchFinally expected = new TryCatchFinally(body, handler, finallyBlock);

        testStatement(TryCatchFinallyVisitor.INSTANCE, "try { aMethod(); } catch { println(); } finally { aMethod(); println(); }", expected);
    }
}
