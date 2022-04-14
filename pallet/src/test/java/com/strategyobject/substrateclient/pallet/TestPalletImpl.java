package com.strategyobject.substrateclient.pallet;

import com.strategyobject.substrateclient.rpc.Rpc;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestPalletImpl implements TestPallet {
    private final Rpc rpc;
}
