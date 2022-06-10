package com.strategyobject.substrateclient.rpc.decoders;

import com.google.gson.Gson;
import com.strategyobject.substrateclient.rpc.EncoderPair;
import com.strategyobject.substrateclient.rpc.RpcEncoder;
import com.strategyobject.substrateclient.rpc.encoders.ListEncoder;
import com.strategyobject.substrateclient.rpc.encoders.MapEncoder;
import com.strategyobject.substrateclient.rpc.encoders.PlainEncoder;
import com.strategyobject.substrateclient.tests.TestSuite;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KnownEncoderTests {

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

    @TestFactory
    Stream<DynamicTest> encode() {
        return TestSuite.of(
                Test.encode(new PlainEncoder<>(), false, false),
                Test.encode(new PlainEncoder<>(), (byte) 5, (byte) 5),
                Test.encode(new PlainEncoder<>(), 15.25, 15.25),
                Test.encode(new PlainEncoder<>(), 19.5f, 19.5f),
                Test.encode(new PlainEncoder<>(), -5, -5),
                Test.encode(new PlainEncoder<>(), 2147483648L, 2147483648L),
                Test.encode(new PlainEncoder<>(), (short) 290, (short) 290),
                Test.encode(new PlainEncoder<>(), "some", "some"),
                Test.encode(new PlainEncoder<>(), (Void) null, null),
                Test.encode(
                                new ListEncoder().inject(EncoderPair.of((source, encoders) -> source.toString(), null)),
                                Arrays.asList(1, 2, 3),
                                Arrays.asList("1", "2", "3"))
                        .withEncoderName(ListEncoder.class.getSimpleName()),
                Test.encode(
                                new MapEncoder().inject(
                                        EncoderPair.of(new PlainEncoder<>(), null),
                                        EncoderPair.of((source, encoders) -> source.toString(), null)),
                                getSourceMap(),
                                getExpectedMap())
                        .withEncoderName(MapEncoder.class.getSimpleName())
        );
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class Test<T> extends TestSuite.TestCase {
        private static final Gson GSON = new Gson();

        private final RpcEncoder<T> encoder;
        private final T given;
        private final Object expected;
        private String encoderName;

        @Override
        public String getDisplayName() {
            return String.format("Encode %s with %s", given, encoderName);
        }

        @Override
        public void execute() {
            val actual = encoder.encode(given);
            assertEquals(GSON.toJson(expected), GSON.toJson(actual));
        }

        public Test<T> withEncoderName(String encoderName) {
            this.encoderName = encoderName;
            return this;
        }

        public static <T> Test<T> encode(RpcEncoder<T> encoder, T given, Object expected) {
            return new Test<>(encoder, given, expected, encoder.getClass().getSimpleName());
        }
    }
}
