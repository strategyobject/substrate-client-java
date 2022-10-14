package com.strategyobject.substrateclient.rpc.api.runtime;

import com.strategyobject.substrateclient.common.types.union.Union;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;

@ScaleReader
public enum LookupError {
    /**
     * / Unknown.
     */
    UNKNOWN,

    /**
     * / Bad Format.
     */
    BAD_FORMAT
}