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
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Blake2B128ConcatTests {
    private static Stream<Arguments> getTestCasesForGetHash() {
        return Stream.of(
                Arguments.of(
                        decode(Integer.class, 5),
                        "0x969e061847da7e84337ea78dc577cd1d05000000"
                ),
                Arguments.of(
                        decode(String.class, "SomeString"),
                        "0x054439e9cc46decbb601602ece91182c28536f6d65537472696e67"
                ),
                Arguments.of(
                        decode(AccountId.class,
                                AccountId.fromBytes(
                                        SS58Codec.decode(
                                                        "5GrwvaEF5zXb26Fz9rcQpDWS57CtERHpNehXCPcNoHGKutQY")
                                                .getAddress())),
                        "0xde1e86a9a8c739864cf3cc5ec2bea59fd43593c715fdd31c61141abd04a99fd6822c8558854ccde39a5684e7a56da27d"
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
        val algorithm = Blake2B128Concat.getInstance();

        val actual = algorithm.getHash(encodedKey);
        val actualInHex = HexConverter.toHex(actual);

        assertEquals(expectedInHex, actualInHex);
    }
}
