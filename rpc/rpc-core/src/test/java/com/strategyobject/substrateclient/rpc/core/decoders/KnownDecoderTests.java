package com.strategyobject.substrateclient.rpc.core.decoders;

import com.google.gson.Gson;
import com.strategyobject.substrateclient.rpc.core.DecoderPair;
import com.strategyobject.substrateclient.rpc.core.RpcDecoder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KnownDecoderTests {
    private final TestCase<?>[] testCases = {
            new TestCase<>(new BooleanDecoder(), "true", true),
            new TestCase<>(new ByteDecoder(), "5", (byte) 5),
            new TestCase<>(new DoubleDecoder(), "15.25", 15.25),
            new TestCase<>(new FloatDecoder(), "19.5", 19.5f),
            new TestCase<>(new IntDecoder(), "-5", -5),
            new TestCase<>(new LongDecoder(), "2147483648", 2147483648L),
            new TestCase<>(new ShortDecoder(), "290", (short) 290),
            new TestCase<>(new StringDecoder(), "\"some\"", "some"),
            new TestCase<>(new VoidDecoder(), "null", null),

            new TestCase<>(
                    new ListDecoder().inject(DecoderPair.of(new IntDecoder(), null)),
                    "[1, 2, 3]",
                    Arrays.asList(1, 2, 3)),
            new TestCase<>(
                    new MapDecoder().inject(
                            DecoderPair.of(new StringDecoder(), null),
                            DecoderPair.of(new IntDecoder(), null)),
                    "{a: 1, b: 2, c: 3}",
                    new HashMap<String, Integer>() {{
                        put("a", 1);
                        put("b", 2);
                        put("c", 3);
                    }}),
    };

    @Test
    public void decode() {
        Gson gson = new Gson();

        Arrays.stream(testCases)
                .forEach(c -> assertEquals(
                        c.decoder.decode(gson.fromJson(c.getJson(), Object.class)),
                        c.getResult()));
    }

    @RequiredArgsConstructor
    @Getter
    static class TestCase<T> {
        private final RpcDecoder<T> decoder;
        private final String json;
        private final T result;
    }
}
