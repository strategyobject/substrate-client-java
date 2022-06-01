package com.strategyobject.substrateclient.common.types;

import com.google.common.base.Preconditions;

/**
 * Sets the size of a class.
 *
 * @param <S> Size
 */
public interface Fixed<S extends Size> {

    /**
     * Asserts that the size is correct.
     *
     * @param expected Expected size
     * @param actual   Actual size
     */
    default void assertSize(S expected, int actual) {
        Preconditions.checkArgument(
                actual == expected.getValue(),
                "Provided size (%s) is invalid. Expected %s.", actual, expected.getValue());
    }
}
