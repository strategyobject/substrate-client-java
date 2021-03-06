package com.strategyobject.substrateclient.pallet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a pallet storage.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Storage {
    /**
     * @return the name of a storage
     */
    String name();

    /**
     * @return the array of items that describe SCALE-codecs
     * and hashing algorithms of storage's keys.
     */
    StorageKey[] keys() default {};
}
