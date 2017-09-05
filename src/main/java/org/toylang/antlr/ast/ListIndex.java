package org.toylang.antlr.ast;

public class ListIndex extends Expression {

    private final QualifiedName name;
    private final Expression[] index;

    public ListIndex(QualifiedName name, Expression... index) {
        this.name = name;
        this.index = index;
    }
    public QualifiedName getName() {
        return name;
    }
    public Expression[] getIndex() {
        return index;
    }
    @Override
    public void accept(TreeVisitor visitor) {
        visitor.visitListIdx(this);
    }
}
