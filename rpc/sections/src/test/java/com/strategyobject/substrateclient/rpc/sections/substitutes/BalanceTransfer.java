package com.strategyobject.substrateclient.rpc.sections.substitutes;

import com.strategyobject.substrateclient.rpc.types.AddressId;
import com.strategyobject.substrateclient.rpc.types.Call;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotations.Scale;
import com.strategyobject.substrateclient.scale.annotations.ScaleWriter;
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
