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
    public int coerceRating(final Class<?> clazz) {
        return COERCE_IMPOSSIBLE;
    }

    @Override
    public boolean equals(final Object o) {
        return o == VOID;
    }
}
