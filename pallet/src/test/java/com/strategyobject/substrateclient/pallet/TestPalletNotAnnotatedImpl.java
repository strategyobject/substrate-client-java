package com.strategyobject.substrateclient.pallet;

import com.strategyobject.substrateclient.rpc.RpcSectionFactory;
import com.strategyobject.substrateclient.rpc.api.section.State;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestPalletNotAnnotatedImpl implements TestPalletNotAnnotated {
    private final ScaleReaderRegistry scaleReaderRegistry;
    private final ScaleWriterRegistry scaleWriterRegistry;
    private final RpcSectionFactory rpcSectionFactory;
}
