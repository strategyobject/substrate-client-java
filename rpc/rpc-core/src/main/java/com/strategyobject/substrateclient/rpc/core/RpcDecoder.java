package com.strategyobject.substrateclient.rpc.core;

public interface RpcDecoder<T> {
    T decode(Object value, DecoderPair<?>... decoders);

    default RpcDecoder<T> inject(DecoderPair<?>... dependencies) {
        return (jsonToken, decoders) -> this.decode(jsonToken, dependencies);
    }
}
