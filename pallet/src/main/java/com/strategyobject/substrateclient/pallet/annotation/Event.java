package com.strategyobject.substrateclient.pallet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a pallet event.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Event {
    /**
     * @return the index of an event
     */
    int index();

    /**
     * Provide a pallet name that corresponds to the event if event class is not defined inside a pallet interface.
     *
     * @return pallet name that corresponds to the event
     */
    String pallet() default "";
}
