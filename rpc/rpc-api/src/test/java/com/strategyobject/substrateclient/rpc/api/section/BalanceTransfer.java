package com.strategyobject.substrateclient.rpc.api.section;

import com.strategyobject.substrateclient.rpc.api.AddressId;
import com.strategyobject.substrateclient.rpc.api.Call;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleWriter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@RequiredArgsConstructor
@Getter
@ScaleWriter
public class BalanceTransfer implements Call {
    private final byte moduleIndex;
    private final byte callIndex;
    private final AddressId destination;
    @Scale(ScaleType.CompactBigInteger.class)
    private final BigInteger amount;
}
