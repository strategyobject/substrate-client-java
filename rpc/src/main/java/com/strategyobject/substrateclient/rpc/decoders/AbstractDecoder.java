package com.strategyobject.substrateclient.rpc.decoders;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.rpc.DecoderPair;
import com.strategyobject.substrateclient.rpc.RpcDecoder;
import com.strategyobject.substrateclient.transport.RpcObject;

public abstract class AbstractDecoder<T> implements RpcDecoder<T> {
    @Override
    public final T decode(RpcObject value, DecoderPair<?>... decoders) {
        if (value == null || value.isNull()) {
            return null;
        }

        checkArguments(value, decoders);

        return decodeNonNull(value, decoders);
    }

    protected abstract T decodeNonNull(RpcObject value, DecoderPair<?>[] decoders);

    protected void checkArguments(RpcObject value, DecoderPair<?>[] decoders) {
        Preconditions.checkArgument(decoders == null || decoders.length == 0);
    }
}
