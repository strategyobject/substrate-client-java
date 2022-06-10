package com.strategyobject.substrateclient.pallet.annotation;

import lombok.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a pallet which represents a proxy to the blockchain's pallet.
 * The processor will generate proper implementations for the interfaces marked with this annotation.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Pallet {
    /**
     * @return the name of a pallet
     */
    @NonNull
    String value();
}
