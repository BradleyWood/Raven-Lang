package org.raven.core.wrappers;

import org.raven.core.Hidden;

public class TType extends TObject {

    @Hidden
    public static TType TYPE = new TType(TType.class);

    @Hidden
    private final Class<?> type;

    @Hidden
    public TType(Class<?> type) {
        this.type = type;
    }

    @Override
    public TObject getType() {
        return TYPE;
    }

    @Override
    public TObject EQ(TObject obj) {
        if (obj instanceof TType) {
            TType type = (TType) obj;
            return this.type == type.type ? TBoolean.TRUE : TBoolean.FALSE;
        }
        return TBoolean.FALSE;
    }

    @Override
    public TObject NE(TObject obj) {
        return EQ(obj).not();
    }

    @Override
    public String toString() {
        return type.getTypeName();
    }

    @Override
    public Object toObject() {
        return type;
    }

    @Override
    public Object coerce(Class clazz) {
        if (clazz.equals(Class.class)) {
            return toObject();
        }
        return super.coerce(clazz);
    }

    @Override
    public int coerceRating(Class clazz) {
        if (clazz.equals(Class.class)) {
            return COERCE_IDEAL;
        }
        return super.coerceRating(clazz);
    }

    @Hidden
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        return o instanceof TType && type.equals(((TType) o).type);
    }

    @Hidden
    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }
}
