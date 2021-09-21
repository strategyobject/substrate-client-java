package com.strategyobject.substrateclient.rpc.core;

public interface RpcEncoded<T extends RpcEncoded<T>> {
    @SuppressWarnings("unchecked")
    default Object encode() throws RpcEncoderNotFoundException {
        return RpcEncoderRegistry
                .getInstance()
                .resolve((Class<T>) this.getClass())
                .encode((T) this);
    }
}
