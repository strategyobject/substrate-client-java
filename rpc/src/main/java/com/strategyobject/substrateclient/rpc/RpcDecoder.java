package com.strategyobject.substrateclient.rpc;

import com.strategyobject.substrateclient.transport.RpcObject;

public interface RpcDecoder<T> {
    T decode(RpcObject value, DecoderPair<?>... decoders);

    default RpcDecoder<T> inject(DecoderPair<?>... dependencies) {
        return (jsonToken, decoders) -> this.decode(jsonToken, dependencies);
    }
}
