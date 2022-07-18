package com.strategyobject.substrateclient.rpc.api.runtime;

import com.strategyobject.substrateclient.scale.annotation.ScaleReader;

/**
 * Arithmetic errors.
 */
@ScaleReader
public enum ArithmeticError {
    /**
     * / Underflow.
     */
    UNDERFLOW,

    /**
     * / Overflow.
     */
    OVERFLOW,

    /**
     * / Division by zero.
     */
    DIVISION_BY_ZERO
}
