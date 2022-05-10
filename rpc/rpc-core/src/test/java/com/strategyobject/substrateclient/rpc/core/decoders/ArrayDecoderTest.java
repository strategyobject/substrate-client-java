package com.strategyobject.substrateclient.rpc.core.decoders;

import com.strategyobject.substrateclient.rpc.core.DecoderPair;
import com.strategyobject.substrateclient.transport.RpcObject;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class ArrayDecoderTest {

    @Test
    void decodeNull() {
        val decoder = new ArrayDecoder().inject(DecoderPair.of(new IntDecoder(), null));
        val rpcObject = RpcObject.ofNull();
        val actual = decoder.decode(rpcObject);

        assertArrayEquals(null, actual);
    }

    @Test
    void decodeNonNull() {
        val decoder = new ArrayDecoder().inject(DecoderPair.of(new IntDecoder(), null));
        val rpcObject = RpcObject.of(Arrays.asList(RpcObject.of(1), RpcObject.ofNull(), RpcObject.of(3)));
        val actual = decoder.decode(rpcObject);

        assertArrayEquals(new Integer[]{1, null, 3}, actual);
    }
}