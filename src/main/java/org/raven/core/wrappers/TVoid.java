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
    public int coerceRating(Class<?> clazz) {
        return COERCE_IMPOSSIBLE;
    }

    @Override
    public boolean equals(Object o) {
        return o == VOID;
    }
}
