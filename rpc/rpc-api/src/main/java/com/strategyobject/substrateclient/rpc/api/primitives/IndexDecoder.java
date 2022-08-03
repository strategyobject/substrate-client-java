package com.strategyobject.substrateclient.rpc.api.primitives;

import com.strategyobject.substrateclient.rpc.DecoderPair;
import com.strategyobject.substrateclient.rpc.annotation.AutoRegister;
import com.strategyobject.substrateclient.rpc.decoders.AbstractDecoder;
import com.strategyobject.substrateclient.transport.RpcObject;

@AutoRegister(types = Index.class)
public class IndexDecoder extends AbstractDecoder<Index> {
    @Override
    protected Index decodeNonNull(RpcObject value, DecoderPair<?>[] decoders) {
        return Index.of(value.asNumber().longValue());
    }
}
