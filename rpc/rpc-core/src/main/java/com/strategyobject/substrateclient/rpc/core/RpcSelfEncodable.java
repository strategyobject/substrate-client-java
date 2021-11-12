package com.strategyobject.substrateclient.rpc.core;

import com.strategyobject.substrateclient.rpc.core.registries.RpcEncoderRegistry;

public interface RpcSelfEncodable<T extends RpcSelfEncodable<T>> {
    @SuppressWarnings("unchecked")
    default Object encode(T source) {
        return ((RpcSelfEncodable<T>) RpcEncoderRegistry.getInstance()
                .resolve(this.getClass()))
                .encode(source);
    }
}
