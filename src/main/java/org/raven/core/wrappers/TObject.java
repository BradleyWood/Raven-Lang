package org.raven.core.wrappers;

import org.raven.core.Hidden;

import java.math.BigInteger;
import java.util.*;

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
    public TObject(Object obj) {
        this.obj = obj;
        this.type = new TType(obj.getClass());
    }

    @Hidden
    public TObject(TType type) {
        this.type = type;
    }

    @Hidden
    public void setType(TType type) {
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
    public TObject set(TObject index, TObject obj) {
        throw new UnsupportedOperationException("Cannot set element in non list type: " + getType().toString());
    }

    @Hidden
    public TObject get(TObject obj) {
        throw new UnsupportedOperationException("Cannot get element from non list type: " + getType().toString());
    }

    @Hidden
    public TObject add(TObject obj) {
        throw new UnsupportedOperationException("Cannot add types '" + getType().toString() + "' and '" + obj.getType().toString() + "'");
    }

    @Hidden
    public TObject sub(TObject obj) {
        throw new UnsupportedOperationException("Cannot subtract types '" + getType().toString() + "' and '" + obj.getType().toString() + "'");
    }

    @Hidden
    public TObject mul(TObject obj) {
        throw new UnsupportedOperationException("Cannot multiply types '" + getType().toString() + "' and '" + obj.getType().toString() + "'");
    }

    @Hidden
    public TObject div(TObject obj) {
        throw new UnsupportedOperationException("Cannot divide types '" + getType().toString() + "' and '" + obj.getType().toString() + "'");
    }

    @Hidden
    public TObject mod(TObject obj) {
        throw new UnsupportedOperationException("Cannot mod types '" + getType().toString() + "' and '" + obj.getType().toString() + "'");
    }

    @Hidden
    public TObject pow(TObject obj) {
        throw new UnsupportedOperationException("Cannot divide types '" + getType().toString() + "' and '" + obj.getType().toString() + "'");
    }

    @Hidden
    public TObject GT(TObject obj) {
        throw new UnsupportedOperationException("Cannot compare types '" + getType().toString() + "' and '" + obj.getType().toString() + "'");
    }

    @Hidden
    public TObject LT(TObject obj) {
        throw new UnsupportedOperationException("Cannot compare types '" + getType().toString() + "' and '" + obj.getType().toString() + "'");
    }

    @Hidden
    public TObject GTE(TObject obj) {
        throw new UnsupportedOperationException("Cannot compare types '" + getType().toString() + "' and '" + obj.getType().toString() + "'");
    }

    @Hidden
    public TObject LTE(TObject obj) {
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
    public TObject EQ(TObject obj) {
        if (this.obj != null && obj.obj != null) {
            return this.obj.equals(obj.obj) ? TBoolean.TRUE : TBoolean.FALSE;
        }
        return TBoolean.FALSE;
    }

    @Hidden
    public TObject NE(TObject obj) {
        return EQ(obj).not();
    }

    public TObject put(TObject key, TObject value) {
        throw new UnsupportedOperationException("Cannot put " + key + ", " + value + " in non-map");
    }

    @Hidden
    public TObject not() {
        throw new UnsupportedOperationException("Cannot invert " + getType().toString());
    }

    @Hidden
    public TObject and(TObject b) {
        return (isTrue() && b.isTrue()) ? TBoolean.TRUE : TBoolean.FALSE;
    }

    @Hidden
    public TObject or(TObject b) {
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

    public Object coerce(Class<?> clazz) {
        if (obj != null && clazz.isAssignableFrom(obj.getClass()) || clazz.equals(Object.class)) {
            return toObject();
        } else if (TObject.class.isAssignableFrom(clazz)) {
            return this;
        }
        throw new UnsupportedOperationException("type " + getType().toString() + " is not coercible to " + clazz);
    }

    public int coerceRating(Class<?> clazz) {
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

    @Hidden
    public static int rate(TObject params, Class<?>[] types) {
        if (!(params instanceof TList) || params.size() != types.length)
            throw new IllegalArgumentException();
        List<TObject> lst = ((TList) params).getList();

        int rating = 0;

        for (int i = 0; i < lst.size(); i++) {
            int r = lst.get(i).coerceRating(types[i]);
            if (r == COERCE_IMPOSSIBLE)
                return COERCE_IMPOSSIBLE;
            rating += r;
        }

        return rating;
    }


    public static Object[] getParams(TObject params, Class<?>[] types, int rating) {
        if (rating == -1) {
            throw new IllegalArgumentException("Cannot coerce arguments: " + rating);
        }
        if (!(params instanceof TList) || params.size() != types.length)
            throw new IllegalArgumentException();

        List<TObject> lst = ((TList) params).getList();
        Object[] ret = new Object[lst.size()];
        for (int i = 0; i < lst.size(); i++) {
            ret[i] = lst.get(i).coerce(types[i]);
        }
        return ret;
    }

    public static Object[] getParams(TObject params, Class<?>[] types) {
        int rating = rate(params, types);
        if (rating == COERCE_IMPOSSIBLE)
            return null;
        return getParams(params, types, rating);
    }

    @Override
    public String toString() {
        return (obj != null ? obj.toString() : "[INVALID-OBJECT]");
    }

    @Override
    public int compareTo(TObject o) {
        if (obj instanceof Comparable && o.obj instanceof Comparable) {
            return ((Comparable) obj).compareTo(o.obj);
        }
        throw new UnsupportedOperationException("Attempted to compare non comparable objects");
    }

    @Override
    public boolean equals(Object o) {
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
