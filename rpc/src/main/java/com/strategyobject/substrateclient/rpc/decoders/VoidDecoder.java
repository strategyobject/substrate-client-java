package com.strategyobject.substrateclient.rpc.decoders;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.rpc.DecoderPair;
import com.strategyobject.substrateclient.rpc.RpcDecoder;
import com.strategyobject.substrateclient.transport.RpcObject;

public class VoidDecoder implements RpcDecoder<Void> {
    @Override
    public Void decode(RpcObject value, DecoderPair<?>... decoders) {
        Preconditions.checkArgument(decoders == null || decoders.length == 0);

        return null;
    }
}
