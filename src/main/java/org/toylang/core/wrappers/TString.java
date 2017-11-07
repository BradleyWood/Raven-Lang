package org.toylang.core.wrappers;


import org.toylang.core.Hidden;

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
        return Integer.parseInt(toString());
    }

    @Override
    public Double toDouble() {
        return Double.parseDouble(toString());
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
