package org.raven.core.wrappers;

import org.raven.core.Hidden;

import java.util.Objects;

public class TError extends TObject {

    public static final TType TYPE = new TType(TError.class);

    private final TString msg;

    public TError(final TString msg) {
        super(TYPE);
        this.msg = msg;
    }

    public TError(final String msg) {
        super(TYPE);
        this.msg = new TString(msg);
    }

    public TString getMessage() {
        return msg;
    }

    @Hidden
    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        return o instanceof TError && Objects.equals(((TError) o).msg, msg);
    }

    @Override
    public int hashCode() {
        return msg.hashCode();
    }

    @Override
    public TObject EQ(final TObject obj) {
        if (equals(obj))
            return TBoolean.TRUE;
        return TBoolean.FALSE;
    }

    @Override
    public TObject NE(final TObject obj) {
        return EQ(obj).not();
    }

    @Override
    public String toString() {
        return "[Error: " + msg + "]";
    }
}
