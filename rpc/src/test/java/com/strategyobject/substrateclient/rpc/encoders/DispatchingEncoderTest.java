package com.strategyobject.substrateclient.rpc.encoders;

import com.strategyobject.substrateclient.rpc.RpcDispatch;
import com.strategyobject.substrateclient.rpc.RpcEncoder;
import com.strategyobject.substrateclient.rpc.registries.RpcEncoderRegistry;
import com.strategyobject.substrateclient.rpc.substitutes.TestDispatch;
import com.strategyobject.substrateclient.rpc.substitutes.TestDispatchEncoder;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DispatchingEncoderTest {
    private final RpcEncoderRegistry registry = new RpcEncoderRegistry() {{
        register(new TestDispatchEncoder(this), TestDispatch.class);
    }};

    @SuppressWarnings("unchecked")
    @Test
    void encode() {
        val encoder = (RpcEncoder<TestDispatch>) registry.resolve(RpcDispatch.class);
        val actual = encoder.encode(new TestDispatch("test"));

        assertEquals("test", actual);
    }
}