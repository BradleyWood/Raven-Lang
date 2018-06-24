package org.raven.core.wrappers;


import org.raven.core.Hidden;

import java.math.BigInteger;

public class TString extends TObject {

    public static TType TYPE = new TType(TString.class);
    @Hidden
    private final String str;

    @Hidden
    public TString(final String str) {
        this.str = str;
    }

    @Override
    public TObject getType() {
        return TYPE;
    }

    @Override
    public TObject add(final TObject obj) {
        return new TString(str + obj);
    }

    @Override
    public TObject EQ(final TObject obj) {
        if (obj == null || !(obj instanceof TString))
            return TBoolean.FALSE;
        return str.equals(obj.toString()) ? TBoolean.TRUE : TBoolean.FALSE;
    }

    @Override
    public TObject GT(final TObject obj) {
        if (obj instanceof TString) {
            return str.compareTo(((TString) obj).str) < 0 ? TBoolean.TRUE : TBoolean.FALSE;
        }
        return super.GT(obj);
    }

    @Override
    public TObject LT(final TObject obj) {
        if (obj instanceof TString) {
            return str.compareTo(((TString) obj).str) > 0 ? TBoolean.TRUE : TBoolean.FALSE;
        }
        return super.GT(obj);
    }

    @Override
    public TObject GTE(final TObject obj) {
        if (obj instanceof TString) {
            return str.compareTo(((TString) obj).str) <= 0 ? TBoolean.TRUE : TBoolean.FALSE;
        }
        return super.GT(obj);
    }

    @Override
    public TObject LTE(final TObject obj) {
        if (obj instanceof TString) {
            return str.compareTo(((TString) obj).str) >= 0 ? TBoolean.TRUE : TBoolean.FALSE;
        }
        return super.GT(obj);
    }

    @Override
    public TObject NE(final TObject obj) {
        return EQ(obj).not();
    }

    @Hidden
    @Override
    public Object toObject() {
        return toString();
    }

    @Override
    public String toString() {
        return str;
    }

    @Override
    public Object coerce(final Class clazz) {
        if (clazz.equals(String.class)) {
            return toObject();
        }
        return super.coerce(clazz);
    }

    @Override
    public int coerceRating(final Class clazz) {
        if (clazz.equals(String.class)) {
            return COERCE_IDEAL;
        }
        return super.coerceRating(clazz);
    }

    @Override
    public int size() {
        return str.length();
    }

    @Override
    public int compareTo(final TObject o) {
        if (o instanceof TString) {
            return this.str.compareTo(o.toString());
        }
        throw new RuntimeException("Cannot compare String with " + o.getClass().getName());
    }

    @Override
    public Integer toInt() {
        try {
            return Integer.parseInt(toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public BigInteger toBigInt() {
        return new BigInteger(str);
    }

    @Override
    public Double toDouble() {
        return Double.parseDouble(toString());
    }

    public String[] split(final String regex) {
        return str.split(regex);
    }

    public String[] split(final String regex, final int limit) {
        return str.split(regex, limit);
    }

    public boolean startsWith(final String prefix) {
        return str.startsWith(prefix);
    }

    public boolean endsWith(final String suffix) {
        return str.endsWith(suffix);
    }

    public String substring(final int beginIndex) {
        return str.substring(beginIndex);
    }

    public String substring(final int beginIndex, final int endIndex) {
        return str.substring(beginIndex, endIndex);
    }

    public String toUpperCase() {
        return str.toUpperCase();
    }

    public String toLowerCase() {
        return str.toLowerCase();
    }

    public boolean contains(final String str) {
        return this.str.contains(str);
    }

    public String trim() {
        return str.trim();
    }

    public String replace(final String target, final String replacement) {
        return str.replace(target, replacement);
    }

    public int indexOf(final String str) {
        return this.str.indexOf(str);
    }

    public boolean isEmpty() {
        return str.isEmpty();
    }

    public boolean matches(final String regex) {
        return str.matches(regex);
    }

    public int lastIndexOf(final String str) {
        return this.str.lastIndexOf(str);
    }

    public boolean equalsIgnoreCase(final String str) {
        return this.str.equalsIgnoreCase(str);
    }

    @Hidden
    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        return o instanceof TString && str.equals(((TString) o).str);
    }

    @Hidden
    @Override
    public int hashCode() {
        return str != null ? str.hashCode() : 0;
    }
}
