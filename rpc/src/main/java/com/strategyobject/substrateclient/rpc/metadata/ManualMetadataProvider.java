package com.strategyobject.substrateclient.rpc.metadata;

import com.strategyobject.substrateclient.crypto.ss58.SS58AddressFormat;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ManualMetadataProvider implements MetadataProvider {
    private final SS58AddressFormat ss58AddressFormat;
    private final PalletCollection palletCollection;

    @Override
    public SS58AddressFormat getSS58AddressFormat() {
        return ss58AddressFormat;
    }

    @Override
    public PalletCollection getPallets() {
        return palletCollection;
    }
}
