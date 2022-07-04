package com.strategyobject.substrateclient.rpc.encoders;

import com.strategyobject.substrateclient.rpc.EncoderPair;
import com.strategyobject.substrateclient.rpc.RpcEncoder;
import com.strategyobject.substrateclient.rpc.registries.RpcEncoderRegistry;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
public class DispatchingEncoder<T> implements RpcEncoder<T> {
    private final @NonNull RpcEncoderRegistry registry;

    @SuppressWarnings("unchecked")
    @Override
    public Object encode(@NonNull T source, EncoderPair<?>... encoders) {
        val encoder = (RpcEncoder<T>) registry.resolve(source.getClass());
        return encoder.encode(source, encoders);
    }
}
