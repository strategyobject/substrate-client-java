package com.strategyobject.substrateclient.rpc.core;

import lombok.NonNull;

public class RpcEncoderNotFoundException extends Exception {
    public <T> RpcEncoderNotFoundException(@NonNull Class<T> clazz) {
        super(String.format("RpcEncoder wasn't registered for %s.", clazz));
    }
}
