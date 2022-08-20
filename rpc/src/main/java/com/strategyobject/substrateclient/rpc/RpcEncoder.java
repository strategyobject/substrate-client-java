package com.strategyobject.substrateclient.rpc;

import com.strategyobject.substrateclient.common.inject.Dependant;

public interface RpcEncoder<T> extends Dependant<RpcEncoder<T>, EncoderPair<?>> {
    Object encode(T source, EncoderPair<?>... encoders);

    @Override
    default RpcEncoder<T> inject(EncoderPair<?>... dependencies) {
        return (source, encoders) -> this.encode(source, dependencies);
    }
}
