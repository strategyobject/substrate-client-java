package com.strategyobject.substrateclient.rpc.api.impl;

import com.strategyobject.substrateclient.common.utils.HexConverter;
import com.strategyobject.substrateclient.rpc.DecoderPair;
import com.strategyobject.substrateclient.rpc.annotation.AutoRegister;
import com.strategyobject.substrateclient.rpc.api.BlockHash;
import com.strategyobject.substrateclient.rpc.api.Hash;
import com.strategyobject.substrateclient.rpc.decoders.AbstractDecoder;
import com.strategyobject.substrateclient.transport.RpcObject;

@AutoRegister(types = {Hash256.class, Hash.class, BlockHash.class})
public class Hash256Decoder extends AbstractDecoder<Hash256> {
    @Override
    protected Hash256 decodeNonNull(RpcObject value, DecoderPair<?>[] decoders) {
        return Hash256.fromBytes(HexConverter.toBytes(value.asString()));
    }
}
