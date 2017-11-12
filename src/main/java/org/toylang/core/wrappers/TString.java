package org.toylang.core.wrappers;


import org.toylang.core.Hidden;

import java.math.BigInteger;

public class TString extends TObject {

    public static TType TYPE = new TType(TString.class);
    @Hidden
    private final String str;

    @Hidden
    public TString(String str) {
        this.str = str;
    }

    @Override
    public TObject getType() {
        return TYPE;
    }

    @Override
    public TObject add(TObject obj) {
        return new TString(str + obj);
    }

    @Override
    public TObject EQ(TObject obj) {
        if (obj == null || !(obj instanceof TString))
            return TBoolean.FALSE;
        return str.equals(obj.toString()) ? TBoolean.TRUE : TBoolean.FALSE;
    }

    @Override
    public TObject GT(TObject obj) {
        if (obj instanceof TString) {
            return str.compareTo(((TString) obj).str) < 0 ? TBoolean.TRUE : TBoolean.FALSE;
        }
        return super.GT(obj);
    }

    @Override
    public TObject LT(TObject obj) {
        if (obj instanceof TString) {
            return str.compareTo(((TString) obj).str) > 0 ? TBoolean.TRUE : TBoolean.FALSE;
        }
        return super.GT(obj);
    }

    @Override
    public TObject GTE(TObject obj) {
        if (obj instanceof TString) {
            return str.compareTo(((TString) obj).str) <= 0 ? TBoolean.TRUE : TBoolean.FALSE;
        }
        return super.GT(obj);
    }

    @Override
    public TObject LTE(TObject obj) {
        if (obj instanceof TString) {
            return str.compareTo(((TString) obj).str) >= 0 ? TBoolean.TRUE : TBoolean.FALSE;
        }
        return super.GT(obj);
    }

    @Override
    public TObject NE(TObject obj) {
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
    public Object coerce(Class clazz) {
        if (clazz.equals(String.class)) {
            return toObject();
        }
        return super.coerce(clazz);
    }

    @Override
    public int coerceRating(Class clazz) {
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
    public int compareTo(TObject o) {
        if (o instanceof TString) {
            String str = o.toString();
            return this.str.compareTo(str);
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

    public String[] split(String regex) {
        return str.split(regex);
    }

    public String[] split(String regex, int limit) {
        return str.split(regex, limit);
    }

    public boolean startsWith(String prefix) {
        return str.startsWith(prefix);
    }

    public boolean endsWith(String suffix) {
        return str.endsWith(suffix);
    }

    public String substring(int beginIndex) {
        return str.substring(beginIndex);
    }

    public String substring(int beginIndex, int endIndex) {
        return str.substring(beginIndex, endIndex);
    }

    public String toUpperCase() {
        return str.toUpperCase();
    }

    public String toLowerCase() {
        return str.toLowerCase();
    }

    public boolean contains(String str) {
        return this.str.contains(str);
    }

    public String trim() {
        return str.trim();
    }

    public String replace(String target, String replacement) {
        return str.replace(target, replacement);
    }

    public int indexOf(String str) {
        return this.str.indexOf(str);
    }

    public boolean isEmpty() {
        return str.isEmpty();
    }

    public boolean matches(String regex) {
        return str.matches(regex);
    }

    public int lastIndexOf(String str) {
        return this.str.lastIndexOf(str);
    }

    public boolean equalsIgnoreCase(String str) {
        return this.str.equalsIgnoreCase(str);
    }

    @Hidden
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TString toyString = (TString) o;

        return str != null ? str.equals(toyString.str) : toyString.str == null;
    }

    @Hidden
    @Override
    public int hashCode() {
        return str != null ? str.hashCode() : 0;
    }
}
