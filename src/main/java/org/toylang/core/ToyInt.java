package org.toylang.core;

public class ToyInt extends ToyObject {

    public static ToyType TYPE = new ToyType(ToyInt.class);
    private static final ToyInt ZERO = new ToyInt(0);

    private int value;

    public ToyInt(int value) {
        this.value = value;
    }
    @Override
    public ToyObject getType() {
        return TYPE;
    }
    public int getValue() {
        return value;
    }
    @Override
    public ToyObject not() {
        return new ToyInt(-value);
    }
    @Override
    public boolean isTrue() {
        return value != 0;
    }
    @Override
    public int compareTo(ToyObject o) {
        if(!(o instanceof ToyInt))
            throw new RuntimeException("Cannot compare int with "+o.getClass().getName());
        return Integer.compare(value, ((ToyInt) o).value);
    }
    @Override
    public ToyObject add(ToyObject obj) {
        if(obj instanceof ToyString) {
            return new ToyString(toString() + obj.toString());
        } else if(obj instanceof ToyReal) {
            return new ToyReal((double)value).add(obj);
        } else if(obj instanceof ToyInt) {
            int other = ((ToyInt) obj).getValue();
            return new ToyInt(value + other);
        }
        return super.add(obj);
    }

    @Override
    public ToyObject sub(ToyObject obj) {
        if(obj instanceof ToyReal) {
            return new ToyReal((double)value).sub(obj);
        } else if(obj instanceof ToyInt) {
            int other = ((ToyInt) obj).getValue();
            return new ToyInt(value - other);
        }
        return super.sub(obj);
    }

    @Override
    public ToyObject mul(ToyObject obj) {
        if(obj instanceof ToyReal) {
            return new ToyReal((double)value).mul(obj);
        } else if(obj instanceof ToyInt) {
            int other = ((ToyInt) obj).getValue();
            return new ToyInt(value * other);
        }
        return super.mul(obj);
    }

    @Override
    public ToyObject div(ToyObject obj) {
        if(obj instanceof ToyReal) {
            return new ToyReal((double)value).div(obj);
        } else if(obj instanceof ToyInt) {
            int other = ((ToyInt) obj).getValue();
            return new ToyInt(value / other);
        }
        return super.div(obj);
    }

    @Override
    public ToyObject mod(ToyObject obj) {
        if(obj instanceof ToyReal) {
            return new ToyReal((double)value).mod(obj);
        } else if(obj instanceof ToyInt) {
            int other = ((ToyInt) obj).getValue();
            return new ToyInt(value % other);
        }
        return super.mod(obj);
    }

    @Override
    public ToyObject pow(ToyObject obj) {
        if(obj instanceof ToyReal) {
            return new ToyReal((double)value).pow(obj);
        } else if(obj instanceof ToyInt) {
            int other = ((ToyInt) obj).getValue();
            if(other < 0)
                return new ToyReal((double)value).pow(new ToyReal(other));

            return new ToyInt((int)Math.pow(value, other));
        }
        return super.pow(obj);
    }

    @Override
    public ToyObject GT(ToyObject obj) {
        if(obj instanceof ToyReal) {
            return new ToyReal(value).GT(obj);
        } else if(obj instanceof ToyInt) {
            int other = ((ToyInt) obj).getValue();
            return value > other ? ToyBoolean.TRUE : ToyBoolean.FALSE;
        }
        return super.GT(obj);
    }

    @Override
    public ToyObject LT(ToyObject obj) {
        if(obj instanceof ToyReal) {
            return new ToyReal(value).LT(obj);
        } else if(obj instanceof ToyInt) {
            int other = ((ToyInt) obj).getValue();
            return value < other ? ToyBoolean.TRUE : ToyBoolean.FALSE;
        }
        return super.LT(obj);
    }

    @Override
    public ToyObject GTE(ToyObject obj) {
        if(obj instanceof ToyReal) {
            return new ToyReal(value).GTE(obj);
        } else if(obj instanceof ToyInt) {
            int other = ((ToyInt) obj).getValue();
            return value >= other ? ToyBoolean.TRUE : ToyBoolean.FALSE;
        }
        return super.GTE(obj);
    }

    @Override
    public ToyObject LTE(ToyObject obj) {
        if(obj instanceof ToyReal) {
            return new ToyReal(value).LTE(obj);
        } else if(obj instanceof ToyInt) {
            int other = ((ToyInt) obj).getValue();
            return value <= other ? ToyBoolean.TRUE : ToyBoolean.FALSE;
        }
        return super.LTE(obj);
    }

    @Override
    public ToyObject EQ(ToyObject obj) {
        if(obj instanceof ToyReal) {
            return new ToyReal(value).EQ(obj);
        } else if(obj instanceof ToyInt) {
            int other = ((ToyInt) obj).getValue();
            return value == other ? ToyBoolean.TRUE : ToyBoolean.FALSE;
        }
        return super.EQ(obj);
    }

    @Override
    public ToyObject NE(ToyObject obj) {
        if(obj instanceof ToyReal) {
            return new ToyReal(value).NE(obj);
        } else if(obj instanceof ToyInt) {
            int other = ((ToyInt) obj).getValue();
            return value != other ? ToyBoolean.TRUE : ToyBoolean.FALSE;
        }
        return super.NE(obj);
    }
    @Override
    public Integer toInt() {
        return value;
    }
    @Hidden
    @Override
    public Byte toByte() {
        return (byte)value;
    }
    @Hidden
    @Override
    public Short toShort() {
        return (short)value;
    }
    @Hidden
    @Override
    public Long toLong() {
        return (long)value;
    }
    @Override
    public Float toFloat() {
        return (float)value;
    }
    @Override
    public Double toDouble() {
        return (double) value;
    }
    @Hidden
    @Override
    public Boolean toBoolean() {
        return isTrue();
    }
    @Hidden
    @Override
    public Object toObject() {
        return value;
    }
    @Override
    public String toString() {
        return Integer.toString(value);
    }
    @Hidden
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(o instanceof ToyObject) {
            ToyObject eq = EQ((ToyObject)o);
            if(eq != null)
                return eq.isTrue();
        }
        return false;
    }
    @Hidden
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + value;
        return result;
    }
}
