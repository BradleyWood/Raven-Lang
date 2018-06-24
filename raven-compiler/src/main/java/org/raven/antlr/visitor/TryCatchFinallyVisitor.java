package org.raven.antlr.visitor;

import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ast.Block;
import org.raven.antlr.ast.QualifiedName;
import org.raven.antlr.ast.TryCatchFinally;

public class TryCatchFinallyVisitor extends RavenBaseVisitor<TryCatchFinally> {

    private TryCatchFinallyVisitor() {
    }

    @Override
    public TryCatchFinally visitTryCatchFinally(final RavenParser.TryCatchFinallyContext ctx) {
        Block body = ctx.block(0).accept(BlockVisitor.INSTANCE);
        Block handler = ctx.block(1).accept(BlockVisitor.INSTANCE);
        QualifiedName exName = ctx.boxedId().accept(BoxedIdVisitor.INSTANCE);
        Block finallyBlock = null;

        if (ctx.block().size() == 3) {
            finallyBlock = ctx.block(2).accept(BlockVisitor.INSTANCE);
        }

        TryCatchFinally tcf = new TryCatchFinally(body, exName, handler, finallyBlock);

        body.setParent(tcf);
        handler.setParent(tcf);
        exName.setParent(tcf);

        return tcf;
    }

    public static final TryCatchFinallyVisitor INSTANCE = new TryCatchFinallyVisitor();
}
