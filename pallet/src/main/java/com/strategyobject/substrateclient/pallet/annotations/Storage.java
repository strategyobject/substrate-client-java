package com.strategyobject.substrateclient.pallet.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates the storage of the pallet.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Storage {
    /**
     * @return the name of the storage
     */
    String value();

    /**
     * @return the array of items which describe SCALE-codecs
     * and hashing algorithms of storage's keys.
     */
    StorageKey[] keys() default {};
}
