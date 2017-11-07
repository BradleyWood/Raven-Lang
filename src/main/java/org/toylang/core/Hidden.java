package org.toylang.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
/**
 * Blocks reflective invocation
 */
public @interface Hidden {

}
