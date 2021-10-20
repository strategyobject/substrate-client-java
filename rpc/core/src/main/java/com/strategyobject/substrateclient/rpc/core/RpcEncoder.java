package com.strategyobject.substrateclient.rpc.core;

public interface RpcEncoder<T> {
    Object encode(T source, EncoderPair<?>... encoders);

    default RpcEncoder<T> inject(EncoderPair<?>... dependencies) {
        return (source, encoders) -> this.encode(source, dependencies);
    }
}
