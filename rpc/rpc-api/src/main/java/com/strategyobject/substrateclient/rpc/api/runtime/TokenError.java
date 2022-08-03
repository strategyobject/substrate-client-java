package com.strategyobject.substrateclient.rpc.api.runtime;

import com.strategyobject.substrateclient.scale.annotation.ScaleReader;

/**
 * Description of what went wrong when trying to complete an operation on a token.
 */
@ScaleReader
public enum TokenError {
    /**
     * / Funds are unavailable.
     */
    NO_FUNDS,

    /**
     * / Account that must exist would die.
     */
    WOULD_DIE,

    /**
     * / Account cannot exist with the funds that would be given.
     */
    BELOW_MINIMUM,

    /**
     * / Account cannot be created.
     */
    CANNOT_CREATE,

    /**
     * / The asset in question is unknown.
     */
    UNKNOWN_ASSET,

    /**
     * / Funds exist but are frozen.
     */
    FROZEN,

    /**
     * / Operation is not supported by the asset.
     */
    UNSUPPORTED,
}
