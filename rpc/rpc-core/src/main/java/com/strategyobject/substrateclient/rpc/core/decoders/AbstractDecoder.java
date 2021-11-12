package com.strategyobject.substrateclient.rpc.core.decoders;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.rpc.core.DecoderPair;
import com.strategyobject.substrateclient.rpc.core.RpcDecoder;

public abstract class AbstractDecoder<T> implements RpcDecoder<T> {
    @Override
    public final T decode(Object value, DecoderPair<?>... decoders) {
        if (value == null) {
            return null;
        }

        // TODO I am not sure should it be the first instruction of the method or not
        checkArguments(value, decoders);

        return decodeNonNull(value, decoders);
    }

    protected abstract T decodeNonNull(Object value, DecoderPair<?>[] decoders);

    protected void checkArguments(Object value, DecoderPair<?>[] decoders) {
        Preconditions.checkArgument(decoders == null || decoders.length == 0);
    }
}
