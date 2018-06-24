package org.raven.core.wrappers;

import org.raven.core.Hidden;

public class TReal extends TObject {

    public static final TType TYPE = new TType(TReal.class);
    @Hidden
    private static final TReal ZERO = new TReal(0.0);

    @Hidden
    private double value;

    public TReal(final double value) {
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
    public TObject inc() {
        value++;
        return this;
    }

    @Override
    public TObject dec() {
        value--;
        return this;
    }

    @Override
    public int compareTo(final TObject o) {
        final TObject result = sub(o);
        if (result instanceof TNull)
            throw new RuntimeException("Cannot compare real with " + o.getClass().getName());
        if (result.LT(ZERO).isTrue())
            return -1;
        else if (result.GT(ZERO).isTrue())
            return 1;
        return 0;
    }

    @Override
    public TObject add(final TObject obj) {
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
    public TObject sub(final TObject obj) {
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
    public TObject mul(final TObject obj) {
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
    public TObject div(final TObject obj) {
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
    public TObject mod(final TObject obj) {
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
    public TObject pow(final TObject obj) {
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
    public TObject GT(final TObject obj) {
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
    public TObject LT(final TObject obj) {
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
    public TObject GTE(final TObject obj) {
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
    public TObject LTE(final TObject obj) {
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
    public TObject EQ(final TObject obj) {
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
    public TObject NE(final TObject obj) {
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
    public Object coerce(final Class clazz) {
        if (clazz.equals(double.class) || clazz.equals(Double.class)) {
            return toDouble();
        } else if (clazz.equals(float.class) || clazz.equals(Float.class)) {
            return toFloat();
        } else if (clazz.equals(int.class) || clazz.equals(Integer.class)) {
            return toInt();
        } else if (clazz.equals(long.class) || clazz.equals(Long.class)) {
            return toLong();
        } else if (clazz.equals(short.class) || clazz.equals(Short.class)) {
            return toShort();
        } else if (clazz.equals(byte.class) || clazz.equals(Byte.class)) {
            return toByte();
        }
        return super.coerce(clazz);
    }

    @Override
    public int coerceRating(final Class clazz) {
        if (clazz.equals(double.class) || clazz.equals(Double.class)
                || clazz.equals(float.class) || clazz.equals(Float.class)) {
            return COERCE_IDEAL;
        } else if (clazz.equals(int.class) || clazz.equals(Integer.class)
                || clazz.equals(long.class) || clazz.equals(Long.class)
                || clazz.equals(short.class) || clazz.equals(Short.class)
                || clazz.equals(byte.class) || clazz.equals(Byte.class)) {
            return COERCE_BAD;
        }
        return super.coerceRating(clazz);
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
    public boolean equals(final Object o) {
        if (this == o) return true;
        return o instanceof TReal && value == ((TReal) o).value;
    }

    @Hidden
    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(value);
        return (int) (temp ^ (temp >>> 32));
    }
}
