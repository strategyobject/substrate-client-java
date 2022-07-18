package com.strategyobject.substrateclient.api.pallet.grandpa;

import com.strategyobject.substrateclient.crypto.PublicKey;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@ScaleReader
@Getter
@Setter
public class Authority {
    private PublicKey authorityId;

    @Scale(ScaleType.U64.class)
    private BigInteger authorityWeight;
}
