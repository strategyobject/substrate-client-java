package com.strategyobject.substrateclient.pallet;

import com.strategyobject.substrateclient.rpc.api.section.State;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestPalletImpl implements TestPallet {
    private final ScaleReaderRegistry scaleReaderRegistry;
    private final ScaleWriterRegistry scaleWriterRegistry;
    private final State state;
}
