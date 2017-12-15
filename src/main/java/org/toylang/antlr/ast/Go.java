package org.toylang.antlr.ast;

public class Go extends Statement {

    private Call goFun;

    /**
     * Initializes a Go statement
     * @param goFun The function to call
     */
    public Go(Call goFun) {
        this.goFun = goFun;
    }

    public Call getGoFun() {
        return goFun;
    }

    public void setGoFun(Call goFun) {
        this.goFun = goFun;
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.visitGo(this);
    }
}
