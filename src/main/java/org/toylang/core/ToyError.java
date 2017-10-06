package org.toylang.core;

public class ToyError extends ToyObject {

    public static final ToyType TYPE = new ToyType(ToyError.class);

    private final ToyString msg;

    public ToyError(ToyString msg) {
        super(TYPE);
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "[Error: " + msg +"]";
    }
}
