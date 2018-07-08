package org.raven.core.wrappers;

import org.raven.core.Hidden;

public class TVoid extends TObject {

    public static final TVoid VOID = new TVoid();

    @Hidden
    public static TType TYPE = new TType(TVoid.class);

    private TVoid() {
        super(TYPE);
    }

    @Override
    public <T> T coerce(final Class<T> clazz) {
        throw new UnsupportedOperationException("Cannot cast void type to " + clazz.getName());
    }

    @Override
    public int coerceRating(final Class<?> clazz) {
        return COERCE_IMPOSSIBLE;
    }

    @Override
    public String toString() {
        return "Void";
    }

    @Override
    public boolean equals(final Object o) {
        return o == VOID;
    }
}
