package org.toylang.antlr.ast;


public class DictDef extends Expression {

    private final Expression[] keys;
    private final Expression[] values;

    public DictDef(Expression[] keys, Expression[] values) {
        this.keys = keys;
        this.values = values;
    }

    public Expression[] getKeys() {
        return keys;
    }

    public Expression[] getValues() {
        return values;
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.visitDictDef(this);
    }
}
