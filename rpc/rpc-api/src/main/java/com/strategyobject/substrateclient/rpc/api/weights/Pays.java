package com.strategyobject.substrateclient.rpc.api.weights;

import com.strategyobject.substrateclient.scale.annotation.ScaleReader;

/**
 * Explicit enum to denote if a transaction pays fee or not.
 */
@ScaleReader
public enum Pays {
    /**
     * Transactor will pay related fees.
     */
    YES,

    /**
     * Transactor will NOT pay related fees.
     */
    NO
}
