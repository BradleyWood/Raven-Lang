package org.raven.core.wrappers;

import org.raven.core.Hidden;

import java.math.BigInteger;

public class TInt extends TObject {

    public static TType TYPE = new TType(TInt.class);
    private static final TInt ZERO = new TInt(0);

    private int value;

    public TInt(int value) {
        this.value = value;
    }

    @Override
    public TObject getType() {
        return TYPE;
    }

    public int getValue() {
        return value;
    }

    @Override
    public TObject not() {
        return new TInt(-value);
    }

    @Override
    public boolean isTrue() {
        return value != 0;
    }

    @Override
    public int compareTo(TObject o) {
        if (!(o instanceof TInt))
            throw new RuntimeException("Cannot compare int with " + o.getClass().getName());
        return Integer.compare(value, ((TInt) o).value);
    }

    @Override
    public TObject inc() {
        value++;
        return this;
    }

    @Override
    public TObject add(TObject obj) {
        if (obj instanceof TString) {
            return new TString(toString() + obj.toString());
        } else if (obj instanceof TReal) {
            return new TReal((double) value).add(obj);
        } else if (obj instanceof TInt) {
            int other = ((TInt) obj).getValue();
            int sum = value + other;
            if (((value ^ sum) & (other ^ sum)) < 0) {
                return new TBigInt(toBigInt().add(obj.toBigInt()));
            }
            return new TInt(sum);
        } else if (obj instanceof TBigInt) {
            return new TBigInt(toBigInt().add(obj.toBigInt()));
        }
        return super.add(obj);
    }

    @Override
    public TObject sub(TObject obj) {
        if (obj instanceof TReal) {
            return new TReal((double) value).sub(obj);
        } else if (obj instanceof TInt) {
            int other = ((TInt) obj).getValue();
            int result = value - other;
            if ((((value ^ other) & (value ^ result)) < 0)) {
                return new TBigInt(toBigInt().subtract(obj.toBigInt()));
            }
            return new TInt(result);
        } else if (obj instanceof TBigInt) {
            return new TBigInt(toBigInt().subtract(obj.toBigInt()));
        }
        return super.sub(obj);
    }

    @Override
    public TObject mul(TObject obj) {
        if (obj instanceof TReal) {
            return new TReal((double) value).mul(obj);
        } else if (obj instanceof TInt) {
            int other = ((TInt) obj).getValue();
            long result = (long) value * (long) other;
            if ((int) result != result) {
                return new TBigInt(toBigInt().multiply(obj.toBigInt()));
            }
            return new TInt((int) result);
        } else if (obj instanceof TBigInt) {
            return new TBigInt(toBigInt().multiply(obj.toBigInt()));
        }
        return super.mul(obj);
    }

    @Override
    public TObject div(TObject obj) {
        if (obj instanceof TReal) {
            return new TReal((double) value).div(obj);
        } else if (obj instanceof TInt) {
            int other = ((TInt) obj).getValue();
            return new TInt(value / other);
        } else if (obj instanceof TBigInt) {
            return new TBigInt(toBigInt().divide(obj.toBigInt()));
        }
        return super.div(obj);
    }

    @Override
    public TObject mod(TObject obj) {
        if (obj instanceof TReal) {
            return new TReal((double) value).mod(obj);
        } else if (obj instanceof TInt) {
            int other = ((TInt) obj).getValue();
            return new TInt(value % other);
        } else if (obj instanceof TBigInt) {
            return new TBigInt(toBigInt().mod(obj.toBigInt()));
        }
        return super.mod(obj);
    }

    @Override
    public TObject pow(TObject obj) {
        if (obj instanceof TReal) {
            return new TReal((double) value).pow(obj);
        } else if (obj instanceof TInt) {
            int other = ((TInt) obj).getValue();
            if (other < 0) {
                return new TReal((double) value).pow(new TReal(other));
            }
            try {
                return new TInt(powExact(value, other));
            } catch (ArithmeticException e) {
                return new TBigInt(toBigInt().pow(other));
            }
        } else if (obj instanceof TBigInt) {
            Integer v = obj.toInt();
            if (v == null)
                throw new ArithmeticException("Exponent " + obj.toString() + " is too big");
            return new TBigInt(toBigInt().pow(v));
        }
        return super.pow(obj);
    }

    private int powExact(int x, int y) throws ArithmeticException {
        if (y >= 0) {
            int z = 1;
            while (true) {
                if ((y & 1) != 0)
                    z = Math.multiplyExact(z, x);
                y >>>= 1;
                if (y == 0)
                    break;
                x = Math.multiplyExact(x, x);
            }
            return z;
        } else {
            if (x == 0)
                throw new ArithmeticException("Negative power of zero is infinity");
            if (x == 1)
                return 1;
            if (x == -1)
                return (y & 1) == 0 ? 1 : -1;
            return 0;
        }
    }

    @Override
    public TObject GT(TObject obj) {
        if (obj instanceof TReal) {
            return new TReal(value).GT(obj);
        } else if (obj instanceof TInt) {
            int other = ((TInt) obj).getValue();
            return value > other ? TBoolean.TRUE : TBoolean.FALSE;
        } else if (obj instanceof TBigInt) {
            return new TBigInt(toBigInt()).GT(obj);
        }
        return super.GT(obj);
    }

    @Override
    public TObject LT(TObject obj) {
        if (obj instanceof TReal) {
            return new TReal(value).LT(obj);
        } else if (obj instanceof TInt) {
            int other = ((TInt) obj).getValue();
            return value < other ? TBoolean.TRUE : TBoolean.FALSE;
        } else if (obj instanceof TBigInt) {
            return new TBigInt(toBigInt()).LT(obj);
        }
        return super.LT(obj);
    }

    @Override
    public TObject GTE(TObject obj) {
        if (obj instanceof TReal) {
            return new TReal(value).GTE(obj);
        } else if (obj instanceof TInt) {
            int other = ((TInt) obj).getValue();
            return value >= other ? TBoolean.TRUE : TBoolean.FALSE;
        } else if (obj instanceof TBigInt) {
            return new TBigInt(toBigInt()).GTE(obj);
        }
        return super.GTE(obj);
    }

    @Override
    public TObject LTE(TObject obj) {
        if (obj instanceof TReal) {
            return new TReal(value).LTE(obj);
        } else if (obj instanceof TInt) {
            int other = ((TInt) obj).getValue();
            return value <= other ? TBoolean.TRUE : TBoolean.FALSE;
        } else if (obj instanceof TBigInt) {
            return new TBigInt(toBigInt()).LTE(obj);
        }
        return super.LTE(obj);
    }

    @Override
    public TObject EQ(TObject obj) {
        if (obj instanceof TReal) {
            return new TReal(value).EQ(obj);
        } else if (obj instanceof TInt) {
            int other = ((TInt) obj).getValue();
            return value == other ? TBoolean.TRUE : TBoolean.FALSE;
        } else if (obj instanceof TBigInt) {
            return obj.EQ(this);
        }
        return super.EQ(obj);
    }

    @Override
    public TObject NE(TObject obj) {
        if (obj instanceof TReal) {
            return new TReal(value).NE(obj);
        } else if (obj instanceof TInt) {
            int other = ((TInt) obj).getValue();
            return value != other ? TBoolean.TRUE : TBoolean.FALSE;
        } else if (obj instanceof TBigInt) {
            return new TBigInt(toBigInt()).NE(obj);
        }
        return super.NE(obj);
    }

    @Override
    public BigInteger toBigInt() {
        return BigInteger.valueOf(value);
    }

    @Override
    public Integer toInt() {
        return value;
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

    @Override
    public Character toChar() {
        return (char) value;
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

    public Object coerce(Class clazz) {
        if (clazz.equals(int.class) || clazz.equals(Integer.class)) {
            return toInt();
        } else if (clazz.equals(long.class) || clazz.equals(Long.class)) {
            return toLong();
        } else if (clazz.equals(short.class) || clazz.equals(Short.class)) {
            return toShort();
        } else if(clazz.equals(byte.class) || clazz.equals(Byte.class)) {
            return toByte();
        } else if (clazz.equals(double.class) || clazz.equals(Double.class)) {
            return toDouble();
        } else if (clazz.equals(float.class) || clazz.equals(Float.class)) {
            return toFloat();
        }
        return super.coerce(clazz);
    }

    @Override
    public int coerceRating(Class clazz) {
        if (clazz.equals(int.class) || clazz.equals(long.class) || clazz.equals(Integer.class) || clazz.equals(Long.class)) {
            return COERCE_IDEAL;
        } else if (clazz.equals(byte.class) || clazz.equals(Byte.class) || clazz.equals(short.class) || clazz.equals(Short.class)) {
            return COERCE_LESS_IDEAL;
        } else if (clazz.equals(float.class) || clazz.equals(Float.class) || clazz.equals(double.class) || clazz.equals(Double.class)) {
            return COERCE_BAD;
        }
        return super.coerceRating(clazz);
    }

    @Hidden
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o instanceof TInt && ((TInt) o).value == value;
    }

    @Hidden
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + value;
        return result;
    }
}
