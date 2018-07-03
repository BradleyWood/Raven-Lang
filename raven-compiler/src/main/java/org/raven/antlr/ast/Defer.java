package org.raven.antlr.ast;

import java.util.Objects;

public class Defer extends Statement {

    private Call call;

    public Defer(final Call call) {
        this.call = call;
    }

    public Call getCall() {
        return call;
    }

    public void setCall(final Call call) {
        this.call = call;
    }

    @Override
    public void accept(final TreeVisitor visitor) {
        visitor.visitDefer(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Defer defer = (Defer) o;
        return Objects.equals(call, defer.call);
    }

    @Override
    public int hashCode() {
        return Objects.hash(call);
    }
}
