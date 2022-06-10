package com.strategyobject.substrateclient.rpc.decoders;

import com.strategyobject.substrateclient.rpc.DecoderPair;
import com.strategyobject.substrateclient.rpc.RpcDecoder;
import com.strategyobject.substrateclient.tests.TestSuite;
import com.strategyobject.substrateclient.transport.RpcObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KnownDecoderTests {

    @TestFactory
    Stream<DynamicTest> decode() {
        return TestSuite.of(
                Test.decode(new BooleanDecoder(), RpcObject.of(true), true),
                Test.decode(new ByteDecoder(), RpcObject.of(5), (byte) 5),
                Test.decode(new DoubleDecoder(), RpcObject.of(15.25), 15.25),
                Test.decode(new FloatDecoder(), RpcObject.of(19.5), 19.5f),
                Test.decode(new IntDecoder(), RpcObject.of(-5), -5),
                Test.decode(new LongDecoder(), RpcObject.of(2147483648L), 2147483648L),
                Test.decode(new ShortDecoder(), RpcObject.of(290), (short) 290),
                Test.decode(new StringDecoder(), RpcObject.of("some"), "some"),
                Test.decode(new ListDecoder().inject(DecoderPair.of(new IntDecoder(), null)),
                                RpcObject.of(Arrays.asList(RpcObject.of(1), RpcObject.ofNull(), RpcObject.of(3))),
                                Arrays.asList(1, null, 3))
                        .withDecoderName(ListDecoder.class.getSimpleName()),
                Test.decode(new MapDecoder().inject(
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
                                }})
                        .withDecoderName(MapDecoder.class.getSimpleName())
        );
    }

    @TestFactory
    Stream<DynamicTest> decodeRpcNull() {
        return TestSuite.of(
                Test.decodeRpcNull(new BooleanDecoder(), null),
                Test.decodeRpcNull(new ByteDecoder(), null),
                Test.decodeRpcNull(new DoubleDecoder(), null),
                Test.decodeRpcNull(new FloatDecoder(), null),
                Test.decodeRpcNull(new IntDecoder(), null),
                Test.decodeRpcNull(new LongDecoder(), null),
                Test.decodeRpcNull(new ShortDecoder(), null),
                Test.decodeRpcNull(new StringDecoder(), null),
                Test.decodeRpcNull(new VoidDecoder(), null),
                Test.decodeRpcNull(
                                new ListDecoder().inject(DecoderPair.of(new IntDecoder(), null)),
                                null)
                        .withDecoderName(ListDecoder.class.getSimpleName()),
                Test.decodeRpcNull(
                                new MapDecoder().inject(
                                        DecoderPair.of(new StringDecoder(), null),
                                        DecoderPair.of(new IntDecoder(), null)),
                                null)
                        .withDecoderName(MapDecoder.class.getSimpleName())
        );
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class Test<T> extends TestSuite.TestCase {
        private final RpcDecoder<T> decoder;
        private final RpcObject given;
        private final T expected;
        private String decoderName;

        @Override
        public String getDisplayName() {
            val name = given.isNull() ? "Decode RpcNull with " : "Decode with ";
            return name + decoderName;
        }

        @Override
        public void execute() {
            val actual = decoder.decode(given);
            assertEquals(expected, actual);
        }

        public Test<T> withDecoderName(String decoderName) {
            this.decoderName = decoderName;
            return this;
        }

        public static <T> Test<T> decode(RpcDecoder<T> decoder, RpcObject rpcObject, T expected) {
            return new Test<>(decoder, rpcObject, expected, decoder.getClass().getSimpleName());
        }

        public static <T> Test<T> decodeRpcNull(RpcDecoder<T> decoder, T expected) {
            return new Test<>(decoder, RpcObject.ofNull(), expected, decoder.getClass().getSimpleName());
        }
    }
}
