package org.raven.antlr;

import org.raven.antlr.ast.TreeVisitor;

public abstract class Node {

    public abstract void accept(TreeVisitor tree);
}
