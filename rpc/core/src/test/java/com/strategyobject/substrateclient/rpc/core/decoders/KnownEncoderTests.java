package com.strategyobject.substrateclient.rpc.core.decoders;

import com.google.gson.Gson;
import com.strategyobject.substrateclient.rpc.core.EncoderPair;
import com.strategyobject.substrateclient.rpc.core.RpcEncoder;
import com.strategyobject.substrateclient.rpc.core.encoders.ListEncoder;
import com.strategyobject.substrateclient.rpc.core.encoders.MapEncoder;
import com.strategyobject.substrateclient.rpc.core.encoders.PlainEncoder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KnownEncoderTests {
    private final TestCase<?>[] testCases = {
            new TestCase<>(new PlainEncoder<>(), false, false),
            new TestCase<>(new PlainEncoder<>(), (byte) 5, (byte) 5),
            new TestCase<>(new PlainEncoder<>(), 15.25, 15.25),
            new TestCase<>(new PlainEncoder<>(), 19.5f, 19.5f),
            new TestCase<>(new PlainEncoder<>(), -5, -5),
            new TestCase<>(new PlainEncoder<>(), 2147483648L, 2147483648L),
            new TestCase<>(new PlainEncoder<>(), (short) 290, (short) 290),
            new TestCase<>(new PlainEncoder<>(), "some", "some"),
            new TestCase<>(new PlainEncoder<>(), (Void) null, null),

            new TestCase<>(
                    new ListEncoder().inject(EncoderPair.of((source, encoders) -> source.toString(), null)),
                    Arrays.asList(1, 2, 3),
                    Arrays.asList("1", "2", "3")),
            new TestCase<>(
                    new MapEncoder().inject(
                            EncoderPair.of(new PlainEncoder<>(), null),
                            EncoderPair.of((source, encoders) -> source.toString(), null)),
                    getSourceMap(),
                    getExpectedMap()),
    };

    private static Map<String, Integer> getSourceMap() {
        val result = new HashMap<String, Integer>();
        result.put("a", 1);
        result.put("b", 2);
        result.put("c", 3);

        return result;
    }

    private static Map<String, String> getExpectedMap() {
        val result = new HashMap<String, String>();
        result.put("a", "1");
        result.put("b", "2");
        result.put("c", "3");

        return result;
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void encode() {
        Gson gson = new Gson();

        Arrays.stream(testCases)
                .forEach(c -> assertEquals(
                        gson.toJson(c.getExpected()),
                        gson.toJson(((RpcEncoder)c.encoder).encode(c.source))));
    }

    @RequiredArgsConstructor
    @Getter
    static class TestCase<T> {
        private final RpcEncoder<T> encoder;
        private final T source;
        private final Object expected;
    }
}
