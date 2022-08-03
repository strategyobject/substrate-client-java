package com.strategyobject.substrateclient.rpc.api;

import com.strategyobject.substrateclient.scale.annotation.ScaleReader;

/**
 * Status of funds.
 */
@ScaleReader
public enum BalanceStatus {
    /**
     * Funds are free, as corresponding to `free` item in Balances.
     */
    FREE,

    /**
     * Funds are reserved, as corresponding to `reserved` item in Balances.
     */
    RESERVED
}
