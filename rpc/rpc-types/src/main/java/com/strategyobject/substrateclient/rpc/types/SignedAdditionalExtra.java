package com.strategyobject.substrateclient.rpc.types;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class SignedAdditionalExtra implements AdditionalExtra {
    private final long specVersion; // as u32
    private final long txVersion; // as u32
    private final BlockHash genesis;
    private final BlockHash eraBlock;

    SignedAdditionalExtra(long specVersion, long txVersion, @NonNull BlockHash genesis, @NonNull BlockHash eraBlock) {
        this.specVersion = specVersion;
        this.txVersion = txVersion;
        this.genesis = genesis;
        this.eraBlock = eraBlock;
    }
}
