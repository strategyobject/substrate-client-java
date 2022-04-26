package com.strategyobject.substrateclient.pallet;

import com.strategyobject.substrateclient.rpc.Rpc;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestPalletNotAnnotatedImpl implements TestPalletNotAnnotated {
    private final Rpc rpc;
}
