package com.strategyobject.substrateclient.api.pallet.polkadot_xcm;

import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;

import java.math.BigInteger;

public class MultiLocation {
    @Scale(ScaleType.U8.class)
    private BigInteger parents;

    @Scale(ScaleType.U128.class)
    private Junctions interior;
}
