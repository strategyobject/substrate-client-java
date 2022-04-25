package com.strategyobject.substrateclient.storage;

import com.strategyobject.substrateclient.rpc.types.BlockHash;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IdentityTests {
    private static Stream<byte[]> getTestCasesForGetHash() {
        return Stream.of(
                encode(Integer.class, -175),
                encode(BlockHash.class, BlockHash.fromBytes(random(32))),
                "TestString".getBytes(StandardCharsets.UTF_8),
                random(new Random().nextInt(128) + 1));
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private static <T> byte[] encode(Class<T> type, T value) {
        val buf = new ByteArrayOutputStream();
        ((ScaleWriter<T>) ScaleWriterRegistry.getInstance().resolve(type)).write(value, buf);

        return buf.toByteArray();
    }

    private static byte[] random(int length) {
        val bytes = new byte[length];
        new Random().nextBytes(bytes);

        return bytes;
    }

    @ParameterizedTest
    @MethodSource("getTestCasesForGetHash")
    public void getHash(byte[] encodedKey) {
        val algorithm = Identity.getInstance();

        val actual = algorithm.getHash(encodedKey);

        assertEquals(encodedKey, actual);
    }
}
