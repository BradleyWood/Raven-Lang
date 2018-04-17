package org.raven.antlr.visitor;

import org.raven.antlr.RavenBaseVisitor;
import org.raven.antlr.RavenParser;
import org.raven.antlr.ast.Block;

public class BlockVisitor extends RavenBaseVisitor<Block> {

    private BlockVisitor() {
    }

    @Override
    public Block visitBlock(final RavenParser.BlockContext ctx) {
        Block block = new Block();

        ctx.statement().forEach(stmt -> block.append(stmt.accept(StatementVisitor.INSTANCE)));

        return block;
    }

    public static final BlockVisitor INSTANCE = new BlockVisitor();
}