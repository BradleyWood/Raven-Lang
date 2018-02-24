package org.toylang.core.wrappers;

import org.toylang.core.Hidden;

public class TBoolean extends TObject {

    public static TType TYPE = new TType(TBoolean.class);
    public static final TBoolean TRUE = new TBoolean(true);
    public static final TBoolean FALSE = new TBoolean(false);

    private boolean value;

    private TBoolean(boolean value) {
        this.value = value;
    }

    @Override
    public TObject getType() {
        return TYPE;
    }

    @Override
    public boolean isTrue() {
        return value;
    }

    @Override
    public TObject not() {
        if (value)
            return FALSE;
        return TRUE;
    }

    @Override
    public Object coerce(Class clazz) {
        if (clazz.equals(boolean.class) || clazz.equals(Boolean.class)) {
            return toBoolean();
        }
        return super.coerce(clazz);
    }

    @Override
    public int coerceRating(Class clazz) {
        if (clazz.equals(boolean.class) || clazz.equals(Boolean.class)) {
            return COERCE_IDEAL;
        }
        return super.coerceRating(clazz);
    }

    @Override
    public Boolean toBoolean() {
        return isTrue();
    }

    @Override
    public Object toObject() {
        return isTrue();
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }

    @Override
    public TObject EQ(TObject obj) {
        if (obj instanceof TBoolean) {
            return (value == ((TBoolean) obj).value) ? TBoolean.TRUE : TBoolean.FALSE;
        }
        return TBoolean.FALSE;
    }

    @Override
    public TObject NE(TObject obj) {
        return EQ(obj).not();
    }

    @Override
    public int compareTo(TObject o) {
        if (!(o instanceof TBoolean))
            throw new RuntimeException("Cannot compare boolean and " + o.getClass().getName());
        TBoolean other = (TBoolean) o;
        return (value == other.value) ? 0 : (value ? 1 : -1);
    }

    @Hidden
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        return o instanceof TBoolean && ((TBoolean) o).value == value;
    }

    @Hidden
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (value ? 1 : 0);
        return result;
    }
}
