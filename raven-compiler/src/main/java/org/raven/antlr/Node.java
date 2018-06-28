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

    public <T extends Node> T getParentByType(final Class<T> type) {
        Node parent = getParent();
        while (parent != null) {
            if (type.isAssignableFrom(parent.getClass()))
                return (T) parent;

            parent = parent.getParent();
        }

        return null;
    }
}
