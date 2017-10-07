package org.toylang.core;


public class ToyString extends ToyObject {

    public static ToyType TYPE = new ToyType(ToyString.class);
    @Hidden
    private final String str;

    @Hidden
    public ToyString(String str) {
        this.str = str;
    }

    @Override
    public ToyObject getType() {
        return TYPE;
    }

    @Override
    public ToyObject add(ToyObject obj) {
        return new ToyString(str + obj);
    }

    @Override
    public ToyObject EQ(ToyObject obj) {
        if (obj == null)
            return ToyBoolean.FALSE;
        return str.equals(obj.toString()) ? ToyBoolean.TRUE : ToyBoolean.FALSE;
    }

    @Override
    public ToyObject NE(ToyObject obj) {
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
    public int compareTo(ToyObject o) {
        if (o instanceof ToyString) {
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

        ToyString toyString = (ToyString) o;

        return str != null ? str.equals(toyString.str) : toyString.str == null;
    }

    @Hidden
    @Override
    public int hashCode() {
        return str != null ? str.hashCode() : 0;
    }
}
