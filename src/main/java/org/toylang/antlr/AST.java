package org.toylang.antlr;

import org.toylang.antlr.ast.TreeVisitor;

public abstract class AST {

    public abstract void accept(TreeVisitor tree);
}
