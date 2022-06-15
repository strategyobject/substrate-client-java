package com.strategyobject.substrateclient.rpc;

import com.strategyobject.substrateclient.rpc.registries.RpcDecoderRegistry;
import com.strategyobject.substrateclient.rpc.registries.RpcEncoderRegistry;
import lombok.val;

public final class RpcRegistryHelper {
    private RpcRegistryHelper() {
    }

    @SuppressWarnings("unchecked")
    public static <T> RpcDecoder<T> resolveAndInjectOrNull(Class<T> clazz, DecoderPair<?>... dependencies) {
        val target = (RpcDecoder<T>) RpcDecoderRegistry.resolve(clazz);

        if (target == null) {
            return null;
        }

        return target.inject(dependencies);
    }

    @SuppressWarnings("unchecked")
    public static <T> RpcEncoder<T> resolveAndInjectOrNull(Class<T> clazz, EncoderPair<?>... dependencies) {
        val target = (RpcEncoder<T>) RpcEncoderRegistry.resolve(clazz);

        if (target == null) {
            return null;
        }

        return target.inject(dependencies);
    }
}
