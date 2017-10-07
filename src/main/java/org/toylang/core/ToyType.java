package org.toylang.core;

public class ToyType extends ToyObject {

    @Hidden
    public static ToyType TYPE = new ToyType(ToyType.class);

    @Hidden
    private final Class<?> type;

    @Hidden
    public ToyType(Class<?> type) {
        this.type = type;
    }
    @Override
    public ToyObject getType() {
        return TYPE;
    }
    @Override
    public ToyObject EQ(ToyObject obj) {
        if(obj instanceof ToyType) {
            ToyType type = (ToyType) obj;
            return this.type == type.type ? ToyBoolean.TRUE : ToyBoolean.FALSE;
        }
        return ToyBoolean.FALSE;
    }
    @Override
    public ToyObject NE(ToyObject obj) {
        return EQ(obj).not();
    }
    @Override
    public String toString() {
        return type.getTypeName();
    }
    @Hidden
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ToyType toyType = (ToyType) o;

        return type != null ? type.equals(toyType.type) : toyType.type == null;
    }
    @Hidden
    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }
}
