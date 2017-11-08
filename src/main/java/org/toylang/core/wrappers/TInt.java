package org.toylang.core.wrappers;

import org.toylang.core.Hidden;

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
    public TObject add(TObject obj) {
        if (obj instanceof TString) {
            return new TString(toString() + obj.toString());
        } else if (obj instanceof TReal) {
            return new TReal((double) value).add(obj);
        } else if (obj instanceof TInt) {
            int other = ((TInt) obj).getValue();
            return new TInt(value + other);
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
            return new TInt(value - other);
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
            return new TInt(value * other);
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
            if (other < 0)
                return new TReal((double) value).pow(new TReal(other));

            return new TInt((int) Math.pow(value, other));
        } else if (obj instanceof TBigInt) {
            return new TBigInt(toBigInt().pow(obj.toInt()));
        }
        return super.pow(obj);
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
        int result = super.hashCode();
        result = 31 * result + value;
        return result;
    }
}
