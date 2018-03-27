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
    public TryCatchFinally visitTryCatchFinally(RavenParser.TryCatchFinallyContext ctx) {
        Block body = ctx.block(0).accept(BlockVisitor.INSTANCE);
        Block handler = ctx.block(1).accept(BlockVisitor.INSTANCE);
        QualifiedName exName = new QualifiedName(ctx.IDENTIFIER().getText());
        Block finallyBlock = null;

        if (ctx.block().size() == 3) {
            finallyBlock = ctx.block(2).accept(BlockVisitor.INSTANCE);
        }

        return new TryCatchFinally(body, exName, handler, finallyBlock);
    }

    public static final TryCatchFinallyVisitor INSTANCE = new TryCatchFinallyVisitor();
}
