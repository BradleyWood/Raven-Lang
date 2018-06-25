package org.raven.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@interface JvmMethod {
    String name() default "";
    String params() default "";
    String ret() default "void";
}
