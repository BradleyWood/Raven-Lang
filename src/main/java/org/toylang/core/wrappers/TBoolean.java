package org.toylang.core.wrappers;

import org.toylang.core.Hidden;

public class ToyBoolean extends ToyObject {

    public static ToyType TYPE = new ToyType(ToyBoolean.class);
    public static final ToyBoolean TRUE = new ToyBoolean(true);
    public static final ToyBoolean FALSE = new ToyBoolean(false);

    private boolean value;

    private ToyBoolean(boolean value) {
        this.value = value;
    }

    @Override
    public ToyObject getType() {
        return TYPE;
    }

    @Override
    public boolean isTrue() {
        return value;
    }

    @Override
    public ToyObject not() {
        if(value)
            return FALSE;
        return TRUE;
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
    public ToyObject EQ(ToyObject obj) {
        if(obj instanceof ToyBoolean) {
            return (value == ((ToyBoolean) obj).value) ? ToyBoolean.TRUE : ToyBoolean.FALSE;
        }
        return ToyBoolean.FALSE;
    }

    @Override
    public ToyObject NE(ToyObject obj) {
        return EQ(obj).not();
    }

    @Override
    public int compareTo(ToyObject o) {
        if (!(o instanceof ToyBoolean))
            throw new RuntimeException("Cannot compare boolean and " + o.getClass().getName());
        ToyBoolean other = (ToyBoolean) o;
        return (value == other.value) ? 0 : (value ? 1 : -1);
    }

    @Hidden
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ToyBoolean that = (ToyBoolean) o;

        return value == that.value;
    }

    @Hidden
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (value ? 1 : 0);
        return result;
    }
}
