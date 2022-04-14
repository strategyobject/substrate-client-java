package com.strategyobject.substrateclient.pallet.annotations;

import com.strategyobject.substrateclient.scale.annotations.Scale;
import com.strategyobject.substrateclient.scale.annotations.ScaleGeneric;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Describes the key's SCALE-codec and hash algorithm.
 */
@Retention(RetentionPolicy.SOURCE)
public @interface StorageKey {
    /**
     * @return the SCALE representation of the key in case it's non-generic.
     */
    Scale type() default @Scale();

    /**
     * @return the SCALE representation of the key in case it's generic.
     */
    ScaleGeneric generic() default @ScaleGeneric(template = "", types = {});

    /**
     * @return the hash algorithm of the key that's used to generate the map's key.
     */
    StorageHasher hasher();
}
