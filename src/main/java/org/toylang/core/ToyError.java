package org.toylang.core;

import java.util.Objects;

public class ToyError extends ToyObject {

    public static final ToyType TYPE = new ToyType(ToyError.class);

    private final ToyString msg;

    public ToyError(ToyString msg) {
        super(TYPE);
        this.msg = msg;
    }

    public ToyError(String msg) {
        super(TYPE);
        this.msg = new ToyString(msg);
    }

    @Hidden
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ToyError toyError = (ToyError) o;
        return Objects.equals(msg, toyError.msg);
    }

    @Override
    public ToyObject EQ(ToyObject obj) {
        if (equals(obj))
            return ToyBoolean.TRUE;
        return ToyBoolean.FALSE;
    }

    @Override
    public ToyObject NE(ToyObject obj) {
        return EQ(obj).not();
    }

    @Override
    public String toString() {
        return "[Error: " + msg + "]";
    }
}
