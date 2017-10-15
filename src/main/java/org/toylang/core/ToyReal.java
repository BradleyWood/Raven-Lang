package org.toylang.core;

public class ToyReal extends ToyObject {

    public static final ToyType TYPE = new ToyType(ToyReal.class);
    @Hidden
    private static final ToyReal ZERO = new ToyReal(0.0);

    @Hidden
    private double value;

    public ToyReal(double value) {
        this.value = value;
    }
    @Override
    public ToyObject getType() {
        return TYPE;
    }
    @Hidden
    public double getValue() {
        return value;
    }
    @Override
    public boolean isTrue() {
        return value != 0.0;
    }
    @Override
    public ToyReal not() {
        return new ToyReal(-value);
    }

    @Override
    public int compareTo(ToyObject o) {
        ToyObject result = sub(o);
        if(result instanceof ToyNull)
            throw new RuntimeException("Cannot compare real with "+o.getClass().getName());
        if(result.LT(ZERO).isTrue())
            return -1;
        else if(result.GT(ZERO).isTrue())
            return 1;
        return 0;
    }
    @Override
    public ToyObject add(ToyObject obj) {
        if(obj instanceof ToyString) {
            return new ToyString(toString() + obj.toString());
        } else if(obj instanceof ToyReal) {
            double other = ((ToyReal) obj).value;
            double result = value + other;
            return new ToyReal(result);
        } else if(obj instanceof ToyInt) {
            double other = (double)((ToyInt) obj).getValue();
            double result = value + other;
            return new ToyReal(result);
        }
        return super.add(obj);
    }
    @Override
    public ToyObject sub(ToyObject obj) {
        if(obj instanceof ToyReal) {
            double other = ((ToyReal) obj).value;
            double result = value - other;
            return new ToyReal(result);
        } else if(obj instanceof ToyInt) {
            double other = (double)((ToyInt) obj).getValue();
            double result = value - other;
            return new ToyReal(result);
        }
        return super.sub(obj);
    }
    @Override
    public ToyObject mul(ToyObject obj) {
        if(obj instanceof ToyReal) {
            double other = ((ToyReal) obj).value;
            double result = value * other;
            return new ToyReal(result);
        } else if(obj instanceof ToyInt) {
            double other = (double)((ToyInt) obj).getValue();
            double result = value * other;
            return new ToyReal(result);
        }
        return super.mul(obj);
    }
    @Override
    public ToyObject div(ToyObject obj) {
        if(obj instanceof ToyReal) {
            double other = ((ToyReal) obj).value;
            double result = value / other;
            return new ToyReal(result);
        } else if(obj instanceof ToyInt) {
            double other = (double)((ToyInt) obj).getValue();
            double result = value / other;
            return new ToyReal(result);
        }
        return super.div(obj);
    }
    @Override
    public ToyObject mod(ToyObject obj) {
        if(obj instanceof ToyReal) {
            double other = ((ToyReal) obj).value;
            double result = value % other;
            return new ToyReal(result);
        } else if(obj instanceof ToyInt) {
            double other = (double)((ToyInt) obj).getValue();
            double result = value % other;
            return new ToyReal(result);
        }
        return super.mod(obj);
    }
    @Override
    public ToyObject pow(ToyObject obj) {
        if(obj instanceof ToyReal) {
            double other = ((ToyReal) obj).value;
            double result = Math.pow(value, other);
            return new ToyReal(result);
        } else if(obj instanceof ToyInt) {
            double other = (double)((ToyInt) obj).getValue();
            double result = Math.pow(value, other);
            return new ToyReal(result);
        }
        return super.pow(obj);
    }
    @Override
    public ToyObject GT(ToyObject obj) {
        if(obj instanceof ToyReal) {
            double other = ((ToyReal) obj).value;
            return value > other ? ToyBoolean.TRUE : ToyBoolean.FALSE;
        } else if(obj instanceof ToyInt) {
            double other = (double)((ToyInt) obj).getValue();
            return value > other ? ToyBoolean.TRUE : ToyBoolean.FALSE;
        }
        return super.GT(obj);
    }
    @Override
    public ToyObject LT(ToyObject obj) {
        if(obj instanceof ToyReal) {
            double other = ((ToyReal) obj).value;
            return value < other ? ToyBoolean.TRUE : ToyBoolean.FALSE;
        } else if(obj instanceof ToyInt) {
            double other = (double)((ToyInt) obj).getValue();
            return value < other ? ToyBoolean.TRUE : ToyBoolean.FALSE;
        }
        return super.LT(obj);
    }
    @Override
    public ToyObject GTE(ToyObject obj) {
        if(obj instanceof ToyReal) {
            double other = ((ToyReal) obj).value;
            return value >= other ? ToyBoolean.TRUE : ToyBoolean.FALSE;
        } else if(obj instanceof ToyInt) {
            double other = (double)((ToyInt) obj).getValue();
            return value >= other ? ToyBoolean.TRUE : ToyBoolean.FALSE;
        }
        return super.GTE(obj);
    }
    @Override
    public ToyObject LTE(ToyObject obj) {
        if(obj instanceof ToyReal) {
            double other = ((ToyReal) obj).value;
            return value <= other ? ToyBoolean.TRUE : ToyBoolean.FALSE;
        } else if(obj instanceof ToyInt) {
            double other = (double)((ToyInt) obj).getValue();
            return value <= other ? ToyBoolean.TRUE : ToyBoolean.FALSE;
        }
        return super.LTE(obj);
    }
    @Override
    public ToyObject EQ(ToyObject obj) {
        if(obj instanceof ToyReal) {
            double other = ((ToyReal) obj).value;
            return value == other ? ToyBoolean.TRUE : ToyBoolean.FALSE;
        } else if(obj instanceof ToyInt) {
            double other = (double)((ToyInt) obj).getValue();
            return value == other ? ToyBoolean.TRUE : ToyBoolean.FALSE;
        }
        return super.EQ(obj);
    }
    @Override
    public ToyObject NE(ToyObject obj) {
        if(obj instanceof ToyReal) {
            double other = ((ToyReal) obj).value;
            return value != other ? ToyBoolean.TRUE : ToyBoolean.FALSE;
        } else if(obj instanceof ToyInt) {
            double other = (double)((ToyInt) obj).getValue();
            return value != other ? ToyBoolean.TRUE : ToyBoolean.FALSE;
        }
        return super.NE(obj);
    }
    @Override
    public Integer toInt() {
        return (int) value;
    }
    @Hidden
    @Override
    public Byte toByte() {
        return (byte) value;
    }
    @Hidden
    @Override
    public Short toShort() {
        return (short) value;
    }
    @Hidden
    @Override
    public Long toLong() {
        return (long) value;
    }
    @Override
    public Float toFloat() {
        return (float) value;
    }
    @Override
    public Double toDouble() {
        return value;
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
        return Double.toString(value);
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
        long temp = Double.doubleToLongBits(value);
        return (int) (temp ^ (temp >>> 32));
    }
}
