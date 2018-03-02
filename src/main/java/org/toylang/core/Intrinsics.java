package org.toylang.core;

import org.toylang.core.wrappers.TNull;
import org.toylang.core.wrappers.TObject;
import org.toylang.core.wrappers.TType;

/**
 * Intrinsic functions that may be called by the compiler
 */
public class Intrinsics {

    /**
     * Requires that an object be a specific type
     * @param obj The object
     * @param type The expected type
     * @param message Error message to be displayed if the types do not match
     */
    public static void requireType(TObject obj, TType type, String message) {
        if (!obj.getType().equals(type)) {
            throw new RuntimeException(message);
        }
    }

    /**
     * Requires that an object is not null. Throws NPE if null
     * @param object The object
     */
    public static void requireNonNull(TObject object) {
        if (object == null || object == TNull.NULL) {
            throw new NullPointerException();
        }
    }
}
