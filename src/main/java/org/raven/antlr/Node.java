package org.raven.antlr;

import org.raven.antlr.ast.TreeVisitor;

public abstract class Node {

    private Node parent;

    public abstract void accept(TreeVisitor tree);

    public Node getParent() {
        return parent;
    }

    public void setParent(final Node parent) {
        this.parent = parent;
    }
}
