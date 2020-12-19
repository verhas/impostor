package com.javax0.impostor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Container annotation for the repeatable {@link Impersonate} annotation.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Impersonates {
    Impersonate[] value();
}
