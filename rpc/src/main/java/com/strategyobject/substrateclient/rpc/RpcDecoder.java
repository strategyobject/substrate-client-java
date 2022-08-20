package com.strategyobject.substrateclient.rpc;

import com.strategyobject.substrateclient.common.inject.Dependant;
import com.strategyobject.substrateclient.transport.RpcObject;

public interface RpcDecoder<T> extends Dependant<RpcDecoder<T>, DecoderPair<?>> {
    T decode(RpcObject value, DecoderPair<?>... decoders);

    @Override
    default RpcDecoder<T> inject(DecoderPair<?>... dependencies) {
        return (jsonToken, decoders) -> this.decode(jsonToken, dependencies);
    }
}
