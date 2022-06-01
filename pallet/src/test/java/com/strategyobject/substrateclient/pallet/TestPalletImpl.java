package com.strategyobject.substrateclient.pallet;

import com.strategyobject.substrateclient.rpc.api.section.State;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestPalletImpl implements TestPallet {
    private final State state;
}
