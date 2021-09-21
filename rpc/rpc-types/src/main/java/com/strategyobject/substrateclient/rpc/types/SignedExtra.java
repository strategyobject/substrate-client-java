package com.strategyobject.substrateclient.rpc.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SignedExtra<E extends Era> implements Extra, SignedExtension {
    private final long specVersion; // ignore
    private final long txVersion; // ignore
    private final BlockHash genesis; // ignore
    private final BlockHash eraBlock; // ignore
    private final E era;
    private final long nonce;
    private final long tip;

    @Override
    public AdditionalExtra getAdditionalExtra() {
        return new SignedAdditionalExtra(specVersion, txVersion, genesis, eraBlock);
    }
}
