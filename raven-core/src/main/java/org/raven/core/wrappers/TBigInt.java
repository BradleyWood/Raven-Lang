package org.raven.core.wrappers;

import java.math.BigInteger;

public class TBigInt extends TObject {

    public static final TType TYPE = new TType(TBigInt.class);

    private final BigInteger value;

    public TBigInt(final int value) {
        this.value = BigInteger.valueOf(value);
    }

    public TBigInt(final long value) {
        this.value = BigInteger.valueOf(value);
    }

    public TBigInt(final String value) {
        this.value = new BigInteger(value);
    }

    public TBigInt(final BigInteger value) {
        this.value = value;
    }

    @Override
    public TObject getType() {
        return TYPE;
    }

    @Override
    public TObject add(final TObject obj) {
        final BigInteger bigInt = obj.toBigInt();
        if (bigInt != null) {
            return new TBigInt(value.add(bigInt));
        }
        return super.add(obj);
    }

    @Override
    public TObject sub(final TObject obj) {
        final BigInteger bigInt = obj.toBigInt();
        if (bigInt != null) {
            return new TBigInt(value.subtract(bigInt));
        }
        return super.sub(obj);
    }

    @Override
    public TObject mul(final TObject obj) {
        final BigInteger bigInt = obj.toBigInt();
        if (bigInt != null) {
            return new TBigInt(value.multiply(bigInt));
        }
        return super.mul(obj);
    }

    @Override
    public TObject div(final TObject obj) {
        final BigInteger bigInt = obj.toBigInt();
        if (bigInt != null) {
            return new TBigInt(value.divide(bigInt));
        }
        return super.div(obj);
    }

    @Override
    public TObject mod(final TObject obj) {
        final BigInteger bigInt = obj.toBigInt();
        if (bigInt != null) {
            return new TBigInt(value.mod(bigInt));
        }
        return super.mod(obj);
    }

    @Override
    public TObject pow(final TObject obj) {
        final Integer bigInt = obj.toInt();
        if (bigInt != null) {
            return new TBigInt(value.pow(bigInt));
        }
        throw new ArithmeticException("Exponent " + obj.toString() + " is too big");
    }

    @Override
    public TObject GT(final TObject obj) {
        final BigInteger bigInt = obj.toBigInt();
        if (bigInt != null) {
            return value.compareTo(bigInt) > 0 ? TBoolean.TRUE : TBoolean.FALSE;
        }
        return super.GT(obj);
    }

    @Override
    public TObject LT(final TObject obj) {
        final BigInteger bigInt = obj.toBigInt();
        if (bigInt != null) {
            return value.compareTo(bigInt) < 0 ? TBoolean.TRUE : TBoolean.FALSE;
        }
        return super.LT(obj);
    }

    @Override
    public TObject GTE(final TObject obj) {
        final BigInteger bigInt = obj.toBigInt();
        if (bigInt != null) {
            return value.compareTo(bigInt) >= 0 ? TBoolean.TRUE : TBoolean.FALSE;
        }
        return super.GTE(obj);
    }

    @Override
    public TObject LTE(final TObject obj) {
        final BigInteger bigInt = obj.toBigInt();
        if (bigInt != null) {
            return value.compareTo(bigInt) <= 0 ? TBoolean.TRUE : TBoolean.FALSE;
        }
        return super.LTE(obj);
    }

    @Override
    public TObject EQ(final TObject obj) {
        final BigInteger bigInt = obj.toBigInt();
        if (bigInt != null) {
            return value.compareTo(bigInt) == 0 ? TBoolean.TRUE : TBoolean.FALSE;
        }
        return super.EQ(obj);
    }

    @Override
    public TObject NE(final TObject obj) {
        final BigInteger bigInt = obj.toBigInt();
        if (bigInt != null) {
            return value.compareTo(bigInt) != 0 ? TBoolean.TRUE : TBoolean.FALSE;
        }
        return super.NE(obj);
    }

    @Override
    public Object coerce(final Class clazz) {
        if (clazz.equals(BigInteger.class)) {
            return this;
        } else if (clazz.equals(long.class) || clazz.equals(Long.class)) {
            try {
                value.longValueExact();
                return toLong();
            } catch (Exception e) {
            }
        }
        return super.coerce(clazz);
    }

    @Override
    public int coerceRating(final Class clazz) {
        if (clazz.equals(BigInteger.class)) {
            return COERCE_IDEAL;
        } else if (clazz.equals(long.class) || clazz.equals(Long.class)) {
            try {
                value.longValueExact();
                return COERCE_IDEAL;
            } catch (Exception e) {
            }
        }
        return super.coerceRating(clazz);
    }

    @Override
    public BigInteger toBigInt() {
        return value;
    }

    @Override
    public TObject not() {
        return new TBigInt(value.not());
    }

    @Override
    public Integer toInt() {
        try {
            return value.intValueExact();
        } catch (ArithmeticException e) {
            return null;
        }
    }

    @Override
    public Byte toByte() {
        return value.byteValueExact();
    }

    @Override
    public Short toShort() {
        return value.shortValueExact();
    }

    @Override
    public Long toLong() {
        return value.longValueExact();
    }

    @Override
    public Float toFloat() {
        return value.floatValue();
    }

    @Override
    public Double toDouble() {
        return value.doubleValue();
    }

    @Override
    public Boolean toBoolean() {
        return EQ(new TBigInt(0)).isTrue();
    }

    @Override
    public int size() {
        return super.size();
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public int compareTo(final TObject o) {
        return value.compareTo(o.toBigInt());
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this)
            return true;
        return o instanceof TBigInt && value.equals(((TBigInt) o).value);
    }

    @Override
    public Object toObject() {
        return value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
