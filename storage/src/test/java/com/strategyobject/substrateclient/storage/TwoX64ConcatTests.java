package com.strategyobject.substrateclient.storage;

import com.strategyobject.substrateclient.common.utils.HexConverter;
import com.strategyobject.substrateclient.crypto.ss58.SS58Codec;
import com.strategyobject.substrateclient.rpc.types.AccountId;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TwoX64ConcatTests {
    private static Stream<Arguments> getTestCasesForGetHash() {
        return Stream.of(
                Arguments.of(
                        decode(Integer.class, 10),
                        "0xa6b274250e6753f00a000000"
                ),
                Arguments.of(
                        "TestString".getBytes(StandardCharsets.UTF_8),
                        "0x3cd20663ed09cfc954657374537472696e67"
                ),
                Arguments.of(
                        "qwerty".getBytes(StandardCharsets.UTF_8),
                        "0x7e918fd4671efe8a717765727479"
                ),
                Arguments.of(
                        "abcd".getBytes(StandardCharsets.UTF_8),
                        "0xcc925dd2b02703de61626364"
                ),
                Arguments.of(
                        "Hello, World!".getBytes(StandardCharsets.UTF_8),
                        "0x7fe40f08f8ac9ac448656c6c6f2c20576f726c6421"
                ),
                Arguments.of(
                        decode(AccountId.class,
                                AccountId.fromBytes(
                                        SS58Codec.decode(
                                                        "5GrwvaEF5zXb26Fz9rcQpDWS57CtERHpNehXCPcNoHGKutQY")
                                                .getAddress())),
                        "0x518366b5b1bc7c99d43593c715fdd31c61141abd04a99fd6822c8558854ccde39a5684e7a56da27d"
                )
        );
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private static <T> byte[] decode(Class<T> type, T value) {
        val buf = new ByteArrayOutputStream();
        ((ScaleWriter<T>) ScaleWriterRegistry.getInstance().resolve(type)).write(value, buf);

        return buf.toByteArray();
    }

    @ParameterizedTest
    @MethodSource("getTestCasesForGetHash")
    public void getHash(byte[] encodedKey, String expectedInHex) {
        val algorithm = new TwoX64Concat();

        val actual = algorithm.getHash(encodedKey);
        val actualInHex = HexConverter.toHex(actual);

        assertEquals(expectedInHex, actualInHex);
    }
}
