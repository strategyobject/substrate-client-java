package com.strategyobject.substrateclient.rpc.api;

import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Ignore;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleWriter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@RequiredArgsConstructor
@Getter
@ScaleWriter
public class SignedExtra<E extends Era> implements Extra, SignedExtension {
    @Ignore
    private final long specVersion;
    @Ignore
    private final long txVersion;
    @Ignore
    private final BlockHash genesis;
    @Ignore
    private final BlockHash eraBlock;
    private final E era;
    @Scale(ScaleType.CompactBigInteger.class)
    private final BigInteger nonce;
    @Scale(ScaleType.CompactBigInteger.class)
    private final BigInteger tip;

    @Override
    public AdditionalExtra getAdditionalExtra() {
        return new SignedAdditionalExtra(specVersion, txVersion, genesis, eraBlock);
    }
}
