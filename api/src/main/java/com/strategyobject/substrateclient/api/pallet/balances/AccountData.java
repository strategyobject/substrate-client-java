package com.strategyobject.substrateclient.api.pallet.balances;

import com.strategyobject.substrateclient.api.pallet.system.System;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@ScaleReader
@Getter
@Setter
public class AccountData implements System.AccountData {
    @Scale(ScaleType.U128.class)
    private BigInteger free;

    @Scale(ScaleType.U128.class)
    private BigInteger reserved;

    @Scale(ScaleType.U128.class)
    private BigInteger miscFrozen;

    @Scale(ScaleType.U128.class)
    private BigInteger feeFrozen;
}
