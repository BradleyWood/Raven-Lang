package org.toylang.antlr.visitor;

import org.toylang.antlr.ToyLangBaseVisitor;
import org.toylang.antlr.ToyLangParser;
import org.toylang.antlr.ast.Block;

public class BlockVisitor extends ToyLangBaseVisitor<Block> {

    private BlockVisitor() {}
    @Override
    public Block visitBlock(ToyLangParser.BlockContext ctx) {
        Block block = new Block();

        ctx.statement().forEach(stmt -> block.append(stmt.accept(StatementVisitor.INSTANCE)));

        return block;
    }
    public static final BlockVisitor INSTANCE = new BlockVisitor();
}