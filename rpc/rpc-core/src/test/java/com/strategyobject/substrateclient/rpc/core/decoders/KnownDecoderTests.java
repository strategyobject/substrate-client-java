package com.strategyobject.substrateclient.rpc.core.decoders;

import com.strategyobject.substrateclient.rpc.core.DecoderPair;
import com.strategyobject.substrateclient.rpc.core.RpcDecoder;
import com.strategyobject.substrateclient.transport.RpcObject;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KnownDecoderTests {
    private final TestCase<?>[] testCases = {
            new TestCase<>(new BooleanDecoder(), RpcObject.of(true), true),
            new TestCase<>(new BooleanDecoder(), RpcObject.ofNull(), null),
            new TestCase<>(new ByteDecoder(), RpcObject.of(5), (byte) 5),
            new TestCase<>(new ByteDecoder(), RpcObject.ofNull(), null),
            new TestCase<>(new DoubleDecoder(), RpcObject.of(15.25), 15.25),
            new TestCase<>(new DoubleDecoder(), RpcObject.ofNull(), null),
            new TestCase<>(new FloatDecoder(), RpcObject.of(19.5), 19.5f),
            new TestCase<>(new FloatDecoder(), RpcObject.ofNull(), null),
            new TestCase<>(new IntDecoder(), RpcObject.of(-5), -5),
            new TestCase<>(new IntDecoder(), RpcObject.ofNull(), null),
            new TestCase<>(new LongDecoder(), RpcObject.of(2147483648L), 2147483648L),
            new TestCase<>(new LongDecoder(), RpcObject.ofNull(), null),
            new TestCase<>(new ShortDecoder(), RpcObject.of(290), (short) 290),
            new TestCase<>(new ShortDecoder(), RpcObject.ofNull(), null),
            new TestCase<>(new StringDecoder(), RpcObject.of("some"), "some"),
            new TestCase<>(new StringDecoder(), RpcObject.ofNull(), null),
            new TestCase<>(new VoidDecoder(), RpcObject.ofNull(), null),
            new TestCase<>(
                    new ListDecoder().inject(DecoderPair.of(new IntDecoder(), null)),
                    RpcObject.of(Arrays.asList(RpcObject.of(1), RpcObject.ofNull(), RpcObject.of(3))),
                    Arrays.asList(1, null, 3)),
            new TestCase<>(
                    new ListDecoder().inject(DecoderPair.of(new IntDecoder(), null)),
                    RpcObject.ofNull(),
                    null),
            new TestCase<>(
                    new MapDecoder().inject(
                            DecoderPair.of(new StringDecoder(), null),
                            DecoderPair.of(new IntDecoder(), null)),
                    RpcObject.of(new HashMap<String, RpcObject>() {{
                        put("a", RpcObject.of(1));
                        put("b", RpcObject.ofNull());
                        put("c", RpcObject.of(3));
                    }}),
                    new HashMap<String, Integer>() {{
                        put("a", 1);
                        put("b", null);
                        put("c", 3);
                    }}),
            new TestCase<>(
                    new MapDecoder().inject(
                            DecoderPair.of(new StringDecoder(), null),
                            DecoderPair.of(new IntDecoder(), null)),
                    RpcObject.ofNull(),
                    null)
    };

    @Test
    public void decode() {
        Arrays.stream(testCases)
                .forEach(c -> assertEquals(c.expected, c.decoder.decode(c.rpcObject)));
    }

    @RequiredArgsConstructor
    static class TestCase<T> {
        private final RpcDecoder<T> decoder;
        private final RpcObject rpcObject;
        private final T expected;
    }
}
