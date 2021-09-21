package com.strategyobject.substrateclient.rpc.sections.substitutes;

import com.strategyobject.substrateclient.rpc.types.AddressId;
import com.strategyobject.substrateclient.rpc.types.Call;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class BalanceTransfer implements Call {
    private final int moduleIndex;
    private final int callIndex;
    private final AddressId destination;
    private final long amount;
}
