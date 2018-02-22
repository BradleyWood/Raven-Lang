package org.toylang.core.wrappers;

import org.toylang.core.Hidden;

import java.util.Objects;

public class TError extends TObject {

    public static final TType TYPE = new TType(TError.class);

    private final TString msg;

    public TError(TString msg) {
        super(TYPE);
        this.msg = msg;
    }

    public TError(String msg) {
        super(TYPE);
        this.msg = new TString(msg);
    }

    public TString getMessage() {
        return msg;
    }

    @Hidden
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TError toyError = (TError) o;
        return Objects.equals(msg, toyError.msg);
    }

    @Override
    public int hashCode() {
        return msg.hashCode();
    }

    @Override
    public TObject EQ(TObject obj) {
        if (equals(obj))
            return TBoolean.TRUE;
        return TBoolean.FALSE;
    }

    @Override
    public TObject NE(TObject obj) {
        return EQ(obj).not();
    }

    @Override
    public String toString() {
        return "[Error: " + msg + "]";
    }
}
