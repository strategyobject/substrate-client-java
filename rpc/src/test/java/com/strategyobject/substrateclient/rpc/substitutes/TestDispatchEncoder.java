package com.strategyobject.substrateclient.rpc.substitutes;

import com.strategyobject.substrateclient.rpc.EncoderPair;
import com.strategyobject.substrateclient.rpc.RpcEncoder;
import com.strategyobject.substrateclient.rpc.registries.RpcEncoderRegistry;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
public class TestDispatchEncoder implements RpcEncoder<TestDispatch> {
    private final RpcEncoderRegistry rpcEncoderRegistry;

    @SuppressWarnings("unchecked")
    @Override
    public Object encode(TestDispatch source, EncoderPair<?>... encoders) {
        val encoder = (RpcEncoder<String>) rpcEncoderRegistry.resolve(String.class);
        return encoder.encode(source.getValue());
    }
}
