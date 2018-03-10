package org.raven.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Blocks reflective invocation
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Hidden {

}
