package org.toylang.core.wrappers;

import java.math.BigInteger;

public class TBigInt extends TObject {

    public static final TType TYPE = new TType(TBigInt.class);

    private final BigInteger value;

    public TBigInt(int value) {
        this.value = BigInteger.valueOf(value);
    }

    public TBigInt(long value) {
        this.value = BigInteger.valueOf(value);
    }

    public TBigInt(String value) {
        this.value = new BigInteger(value);
    }

    public TBigInt(BigInteger value) {
        this.value = value;
    }

    @Override
    public TObject getType() {
        return TYPE;
    }

    @Override
    public TObject add(TObject obj) {
        BigInteger bigInt = obj.toBigInt();
        if (bigInt != null) {
            return new TBigInt(value.add(bigInt));
        }
        return super.add(obj);
    }

    @Override
    public TObject sub(TObject obj) {
        BigInteger bigInt = obj.toBigInt();
        if (bigInt != null) {
            return new TBigInt(value.subtract(bigInt));
        }
        return super.sub(obj);
    }

    @Override
    public TObject mul(TObject obj) {
        BigInteger bigInt = obj.toBigInt();
        if (bigInt != null) {
            return new TBigInt(value.multiply(bigInt));
        }
        return super.mul(obj);
    }

    @Override
    public TObject div(TObject obj) {
        BigInteger bigInt = obj.toBigInt();
        if (bigInt != null) {
            return new TBigInt(value.divide(bigInt));
        }
        return super.div(obj);
    }

    @Override
    public TObject mod(TObject obj) {
        BigInteger bigInt = obj.toBigInt();
        if (bigInt != null) {
            return new TBigInt(value.mod(bigInt));
        }
        return super.mod(obj);
    }

    @Override
    public TObject pow(TObject obj) {
        Integer bigInt = obj.toInt();
        if (bigInt != null) {
            return new TBigInt(value.pow(bigInt));
        }
        return super.pow(obj);
    }

    @Override
    public TObject GT(TObject obj) {
        BigInteger bigInt = obj.toBigInt();
        if (bigInt != null) {
            return value.compareTo(bigInt) < 0 ? TBoolean.TRUE : TBoolean.FALSE;
        }
        return super.GT(obj);
    }

    @Override
    public TObject LT(TObject obj) {
        BigInteger bigInt = obj.toBigInt();
        if (bigInt != null) {
            return value.compareTo(bigInt) > 0 ? TBoolean.TRUE : TBoolean.FALSE;
        }
        return super.LT(obj);
    }

    @Override
    public TObject GTE(TObject obj) {
        BigInteger bigInt = obj.toBigInt();
        if (bigInt != null) {
            return value.compareTo(bigInt) <= 0 ? TBoolean.TRUE : TBoolean.FALSE;
        }
        return super.GTE(obj);
    }

    @Override
    public TObject LTE(TObject obj) {
        BigInteger bigInt = obj.toBigInt();
        if (bigInt != null) {
            return value.compareTo(bigInt) >= 0 ? TBoolean.TRUE : TBoolean.FALSE;
        }
        return super.LTE(obj);
    }

    @Override
    public TObject EQ(TObject obj) {
        BigInteger bigInt = obj.toBigInt();
        if (bigInt != null) {
            return value.compareTo(bigInt) == 0 ? TBoolean.TRUE : TBoolean.FALSE;
        }
        return super.EQ(obj);
    }

    @Override
    public TObject NE(TObject obj) {
        BigInteger bigInt = obj.toBigInt();
        if (bigInt != null) {
            return value.compareTo(bigInt) != 0 ? TBoolean.TRUE : TBoolean.FALSE;
        }
        return super.NE(obj);
    }

    @Override
    public BigInteger toBigInt() {
        return value;
    }

    @Override
    public TObject put(TObject key, TObject value) {
        return super.put(key, value);
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
    public int compareTo(TObject o) {
        return value.compareTo(o.toBigInt());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TObject) {
            BigInteger bigInt = ((TObject) o).toBigInt();
            return value.equals(bigInt);
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
