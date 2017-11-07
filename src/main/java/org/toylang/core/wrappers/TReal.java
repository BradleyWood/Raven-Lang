package org.toylang.core.wrappers;

import org.toylang.core.Hidden;

public class TReal extends TObject {

    public static final TType TYPE = new TType(TReal.class);
    @Hidden
    private static final TReal ZERO = new TReal(0.0);

    @Hidden
    private double value;

    public TReal(double value) {
        this.value = value;
    }

    @Override
    public TObject getType() {
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
    public TReal not() {
        return new TReal(-value);
    }

    @Override
    public int compareTo(TObject o) {
        TObject result = sub(o);
        if (result instanceof TNull)
            throw new RuntimeException("Cannot compare real with " + o.getClass().getName());
        if (result.LT(ZERO).isTrue())
            return -1;
        else if (result.GT(ZERO).isTrue())
            return 1;
        return 0;
    }

    @Override
    public TObject add(TObject obj) {
        if (obj instanceof TString) {
            return new TString(toString() + obj.toString());
        } else if (obj instanceof TReal) {
            double other = ((TReal) obj).value;
            double result = value + other;
            return new TReal(result);
        } else if (obj instanceof TInt) {
            double other = (double) ((TInt) obj).getValue();
            double result = value + other;
            return new TReal(result);
        }
        return super.add(obj);
    }

    @Override
    public TObject sub(TObject obj) {
        if (obj instanceof TReal) {
            double other = ((TReal) obj).value;
            double result = value - other;
            return new TReal(result);
        } else if (obj instanceof TInt) {
            double other = (double) ((TInt) obj).getValue();
            double result = value - other;
            return new TReal(result);
        }
        return super.sub(obj);
    }

    @Override
    public TObject mul(TObject obj) {
        if (obj instanceof TReal) {
            double other = ((TReal) obj).value;
            double result = value * other;
            return new TReal(result);
        } else if (obj instanceof TInt) {
            double other = (double) ((TInt) obj).getValue();
            double result = value * other;
            return new TReal(result);
        }
        return super.mul(obj);
    }

    @Override
    public TObject div(TObject obj) {
        if (obj instanceof TReal) {
            double other = ((TReal) obj).value;
            double result = value / other;
            return new TReal(result);
        } else if (obj instanceof TInt) {
            double other = (double) ((TInt) obj).getValue();
            double result = value / other;
            return new TReal(result);
        }
        return super.div(obj);
    }

    @Override
    public TObject mod(TObject obj) {
        if (obj instanceof TReal) {
            double other = ((TReal) obj).value;
            double result = value % other;
            return new TReal(result);
        } else if (obj instanceof TInt) {
            double other = (double) ((TInt) obj).getValue();
            double result = value % other;
            return new TReal(result);
        }
        return super.mod(obj);
    }

    @Override
    public TObject pow(TObject obj) {
        if (obj instanceof TReal) {
            double other = ((TReal) obj).value;
            double result = Math.pow(value, other);
            return new TReal(result);
        } else if (obj instanceof TInt) {
            double other = (double) ((TInt) obj).getValue();
            double result = Math.pow(value, other);
            return new TReal(result);
        }
        return super.pow(obj);
    }

    @Override
    public TObject GT(TObject obj) {
        if (obj instanceof TReal) {
            double other = ((TReal) obj).value;
            return value > other ? TBoolean.TRUE : TBoolean.FALSE;
        } else if (obj instanceof TInt) {
            double other = (double) ((TInt) obj).getValue();
            return value > other ? TBoolean.TRUE : TBoolean.FALSE;
        }
        return super.GT(obj);
    }

    @Override
    public TObject LT(TObject obj) {
        if (obj instanceof TReal) {
            double other = ((TReal) obj).value;
            return value < other ? TBoolean.TRUE : TBoolean.FALSE;
        } else if (obj instanceof TInt) {
            double other = (double) ((TInt) obj).getValue();
            return value < other ? TBoolean.TRUE : TBoolean.FALSE;
        }
        return super.LT(obj);
    }

    @Override
    public TObject GTE(TObject obj) {
        if (obj instanceof TReal) {
            double other = ((TReal) obj).value;
            return value >= other ? TBoolean.TRUE : TBoolean.FALSE;
        } else if (obj instanceof TInt) {
            double other = (double) ((TInt) obj).getValue();
            return value >= other ? TBoolean.TRUE : TBoolean.FALSE;
        }
        return super.GTE(obj);
    }

    @Override
    public TObject LTE(TObject obj) {
        if (obj instanceof TReal) {
            double other = ((TReal) obj).value;
            return value <= other ? TBoolean.TRUE : TBoolean.FALSE;
        } else if (obj instanceof TInt) {
            double other = (double) ((TInt) obj).getValue();
            return value <= other ? TBoolean.TRUE : TBoolean.FALSE;
        }
        return super.LTE(obj);
    }

    @Override
    public TObject EQ(TObject obj) {
        if (obj instanceof TReal) {
            double other = ((TReal) obj).value;
            return value == other ? TBoolean.TRUE : TBoolean.FALSE;
        } else if (obj instanceof TInt) {
            double other = (double) ((TInt) obj).getValue();
            return value == other ? TBoolean.TRUE : TBoolean.FALSE;
        }
        return super.EQ(obj);
    }

    @Override
    public TObject NE(TObject obj) {
        if (obj instanceof TReal) {
            double other = ((TReal) obj).value;
            return value != other ? TBoolean.TRUE : TBoolean.FALSE;
        } else if (obj instanceof TInt) {
            double other = (double) ((TInt) obj).getValue();
            return value != other ? TBoolean.TRUE : TBoolean.FALSE;
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
        if (o instanceof TObject) {
            TObject eq = EQ((TObject) o);
            if (eq != null)
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
