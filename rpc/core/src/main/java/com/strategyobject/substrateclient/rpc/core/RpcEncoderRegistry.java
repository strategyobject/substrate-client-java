package com.strategyobject.substrateclient.rpc.core;

import lombok.NonNull;

public class RpcEncoderRegistry {
    public <T> RpcEncoder<T> resolve(@NonNull Class<T> clazz) throws RpcEncoderNotFoundException {
        throw new RpcEncoderNotFoundException(clazz); // TODO It will be done later.
    }

    public static RpcEncoderRegistry getInstance() {
        throw new UnsupportedOperationException(); // TODO It will be done later.
    }
}
