package com.strategyobject.substrateclient.api.pallet.proxy;

import com.strategyobject.substrateclient.scale.annotation.ScaleReader;

/**
 * The type used to represent the kinds of proxying allowed.
 */
@ScaleReader
public enum ProxyType {
    ANY,
    NON_TRANSFER,
    GOVERNANCE,
    STAKING
}
