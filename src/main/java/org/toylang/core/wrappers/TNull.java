package org.toylang.core.wrappers;

import org.toylang.core.Hidden;

public class ToyNull extends ToyObject {

    @Hidden
    public static final ToyType TYPE = new ToyType(ToyNull.class);

    public static final ToyNull NULL = new ToyNull();

    @Hidden
    private ToyNull() {
    }
    @Override
    public ToyObject getType() {
        return TYPE;
    }
    @Override
    public boolean isTrue() {
        return super.isTrue();
    }
    @Hidden
    @Override
    public Object toObject() {
        return null;
    }

    @Override
    public int compareTo(ToyObject o) {
        if(!(o instanceof ToyNull))
            return 0;
        throw new RuntimeException("Cannot compare null with "+o.getClass().getName());
    }

    @Override
    public ToyObject EQ(ToyObject obj) {
        return ((obj == null) || (obj instanceof ToyNull)) ? ToyBoolean.TRUE : ToyBoolean.FALSE;
    }
    @Override
    public ToyObject NE(ToyObject obj) {
        return EQ(obj).not();
    }
    @Override
    public String toString() {
        return "null";
    }
    @Hidden
    @Override
    public boolean equals(Object o) {
        return o instanceof ToyNull;
    }
}
