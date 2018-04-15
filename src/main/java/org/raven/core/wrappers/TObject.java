package org.raven.core.wrappers;

import org.raven.core.Hidden;

import java.math.BigInteger;

public class TObject implements Comparable<TObject> {

    public static final int COERCE_IMPOSSIBLE = 0;
    public static final int COERCE_BAD = 1;
    public static final int COERCE_LESS_IDEAL = 2;
    public static final int COERCE_IDEAL = 3;

    @Hidden
    private TType type;
    @Hidden
    private Object obj = null;

    @Hidden
    protected TObject() {
    }

    @Hidden
    public TObject(final Object obj) {
        this.obj = obj;
        this.type = new TType(obj.getClass());
    }

    @Hidden
    public TObject(final TType type) {
        this.type = type;
    }

    @Hidden
    public void setType(final TType type) {
        this.type = type;
    }

    public TObject getType() {
        return type;
    }

    @Hidden
    public boolean isTrue() {
        if (obj instanceof Boolean) {
            return (boolean) obj;
        }
        return false;
    }

    @Hidden
    public Object getObject() {
        return obj;
    }

    @Hidden
    public TObject set(final TObject index, final TObject obj) {
        throw new UnsupportedOperationException("Cannot set element in non list type: " + getType().toString());
    }

    @Hidden
    public TObject get(final TObject obj) {
        throw new UnsupportedOperationException("Cannot get element from non list type: " + getType().toString());
    }

    @Hidden
    public TObject add(final TObject obj) {
        throw new UnsupportedOperationException("Cannot add types '" + getType().toString() + "' and '" + obj.getType().toString() + "'");
    }

    @Hidden
    public TObject sub(final TObject obj) {
        throw new UnsupportedOperationException("Cannot subtract types '" + getType().toString() + "' and '" + obj.getType().toString() + "'");
    }

    @Hidden
    public TObject mul(final TObject obj) {
        throw new UnsupportedOperationException("Cannot multiply types '" + getType().toString() + "' and '" + obj.getType().toString() + "'");
    }

    @Hidden
    public TObject div(final TObject obj) {
        throw new UnsupportedOperationException("Cannot divide types '" + getType().toString() + "' and '" + obj.getType().toString() + "'");
    }

    @Hidden
    public TObject mod(final TObject obj) {
        throw new UnsupportedOperationException("Cannot mod types '" + getType().toString() + "' and '" + obj.getType().toString() + "'");
    }

    @Hidden
    public TObject pow(final TObject obj) {
        throw new UnsupportedOperationException("Cannot divide types '" + getType().toString() + "' and '" + obj.getType().toString() + "'");
    }

    @Hidden
    public TObject GT(final TObject obj) {
        throw new UnsupportedOperationException("Cannot compare types '" + getType().toString() + "' and '" + obj.getType().toString() + "'");
    }

    @Hidden
    public TObject LT(final TObject obj) {
        throw new UnsupportedOperationException("Cannot compare types '" + getType().toString() + "' and '" + obj.getType().toString() + "'");
    }

    @Hidden
    public TObject GTE(final TObject obj) {
        throw new UnsupportedOperationException("Cannot compare types '" + getType().toString() + "' and '" + obj.getType().toString() + "'");
    }

    @Hidden
    public TObject LTE(final TObject obj) {
        throw new UnsupportedOperationException("Cannot compare types '" + getType().toString() + "' and '" + obj.getType().toString() + "'");
    }

    @Hidden
    public TObject inc() {
        throw new UnsupportedOperationException("Cannot inc type '" + getType().toString());
    }

    @Hidden
    public TObject dec() {
        throw new UnsupportedOperationException("Cannot dec type '" + getType().toString());
    }

    @Hidden
    public TObject EQ(final TObject obj) {
        if (this.obj != null && obj.obj != null) {
            return this.obj.equals(obj.obj) ? TBoolean.TRUE : TBoolean.FALSE;
        }
        return TBoolean.FALSE;
    }

    @Hidden
    public TObject NE(final TObject obj) {
        return EQ(obj).not();
    }

    public TObject put(final TObject key, final TObject value) {
        throw new UnsupportedOperationException("Cannot put " + key + ", " + value + " in non-map");
    }

    @Hidden
    public TObject not() {
        throw new UnsupportedOperationException("Cannot invert " + getType().toString());
    }

    @Hidden
    public TObject and(final TObject b) {
        return (isTrue() && b.isTrue()) ? TBoolean.TRUE : TBoolean.FALSE;
    }

    @Hidden
    public TObject or(final TObject b) {
        return (isTrue() || b.isTrue()) ? TBoolean.TRUE : TBoolean.FALSE;
    }

    public Integer toInt() {
        return null;
    }

    @Hidden
    public Byte toByte() {
        return null;
    }

    @Hidden
    public Short toShort() {
        return null;
    }

    public Character toChar() {
        return null;
    }

    @Hidden
    public Long toLong() {
        return null;
    }

    @Hidden
    public Float toFloat() {
        return null;
    }

    public Double toDouble() {
        return null;
    }

    @Hidden
    public Boolean toBoolean() {
        throw new UnsupportedOperationException(this + " cannot be converted to boolean");
    }

    public BigInteger toBigInt() {
        return null;
    }

    public int size() {
        return 0;
    }

    @Hidden
    public Object toObject() {
        return obj;
    }

    @Hidden
    public Object[] toArray() {
        throw new UnsupportedOperationException(this + " cannot be converted to array");
    }

    public Object coerce(final Class<?> clazz) {
        if (obj != null && clazz.isAssignableFrom(obj.getClass()) || clazz.equals(Object.class)) {
            return toObject();
        } else if (TObject.class.isAssignableFrom(clazz)) {
            return this;
        }
        throw new UnsupportedOperationException("type " + getType().toString() + " is not coercible to " + clazz);
    }

    public int coerceRating(final Class<?> clazz) {
        if (TObject.class.isAssignableFrom(clazz)) {
            return COERCE_IDEAL;
        }
        if (obj != null && clazz.isAssignableFrom(obj.getClass())) {
            return COERCE_IDEAL;
        }
        if (clazz.equals(Object.class)) {
            return COERCE_BAD;
        }
        return COERCE_IMPOSSIBLE;
    }

    @Override
    public String toString() {
        return (obj != null ? obj.toString() : "[INVALID-OBJECT]");
    }

    @Override
    public int compareTo(final TObject o) {
        if (obj instanceof Comparable && o.obj instanceof Comparable) {
            return ((Comparable) obj).compareTo(o.obj);
        }
        throw new UnsupportedOperationException("Attempted to compare non comparable objects");
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (!(o instanceof TObject))
            return false;
        TObject to = (TObject) o;
        return getType().equals(to.getType()) && obj.equals(to.obj);
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (obj != null ? obj.hashCode() : 0);
        return result;
    }
}
