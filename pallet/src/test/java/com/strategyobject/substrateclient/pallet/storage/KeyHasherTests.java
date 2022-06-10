package com.strategyobject.substrateclient.pallet.storage;

import com.strategyobject.substrateclient.common.utils.HexConverter;
import com.strategyobject.substrateclient.crypto.ss58.SS58Codec;
import com.strategyobject.substrateclient.rpc.api.AccountId;
import com.strategyobject.substrateclient.rpc.api.BlockHash;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.scale.readers.CompactIntegerReader;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import com.strategyobject.substrateclient.scale.writers.CompactIntegerWriter;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class KeyHasherTests {
    private static Stream<Arguments> getTestCasesForGetHash() {
        return Stream.of(
                Arguments.of(
                        10,
                        ScaleReaderRegistry.getInstance().resolve(Integer.class),
                        ScaleWriterRegistry.getInstance().resolve(Integer.class),
                        TwoX64Concat.getInstance(),
                        "0xa6b274250e6753f00a000000"
                ),
                Arguments.of(
                        AccountId.fromBytes(
                                SS58Codec.decode(
                                                "5FHneW46xGXgs5mUiveU4sbTyGBzmstUspZC92UhjJM694ty")
                                        .getAddress()),
                        ScaleReaderRegistry.getInstance().resolve(AccountId.class),
                        ScaleWriterRegistry.getInstance().resolve(AccountId.class),
                        Blake2B128Concat.getInstance(),
                        "0x4f9aea1afa791265fae359272badc1cf8eaf04151687736326c9fea17e25fc5287613693c912909cb226aa4794f26a48"
                ),
                Arguments.of(
                        BlockHash.fromBytes(HexConverter.toBytes("0x0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef")),
                        ScaleReaderRegistry.getInstance().resolve(BlockHash.class),
                        ScaleWriterRegistry.getInstance().resolve(BlockHash.class),
                        Identity.getInstance(),
                        "0x0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef"
                )
        );
    }

    private static Stream<Arguments> getTestCasesForExtractKey() {
        return Stream.of(
                Arguments.of(
                        new ByteArrayInputStream(
                                HexConverter.toBytes("0x5153cb1f00942ff401000000")
                        ),
                        ScaleReaderRegistry.getInstance().resolve(Integer.class),
                        ScaleWriterRegistry.getInstance().resolve(Integer.class),
                        TwoX64Concat.getInstance(),
                        1,
                        0
                ),
                Arguments.of(
                        new ByteArrayInputStream(
                                HexConverter.toBytes("0x969e061847da7e84337ea78dc577cd1d05000000")
                        ),
                        ScaleReaderRegistry.getInstance().resolve(Integer.class),
                        ScaleWriterRegistry.getInstance().resolve(Integer.class),
                        Blake2B128Concat.getInstance(),
                        5,
                        0
                ),
                Arguments.of(
                        new ByteArrayInputStream(
                                HexConverter.toBytes("0xa8")
                        ),
                        new CompactIntegerReader(),
                        new CompactIntegerWriter(),
                        Identity.getInstance(),
                        42,
                        0
                ),
                Arguments.of(
                        new ByteArrayInputStream(
                                HexConverter.toBytes("0x")
                        ),
                        ScaleReaderRegistry.getInstance().resolve(Void.class),
                        ScaleWriterRegistry.getInstance().resolve(Void.class),
                        Identity.getInstance(),
                        null,
                        0
                ),
                Arguments.of(
                        new ByteArrayInputStream(
                                HexConverter.toBytes("0x4f9aea1afa791265fae359272badc1cf8eaf04151687736326c9fea17e25fc5287613693c912909cb226aa4794f26a48" +
                                        "a6b274250e6753f00a000000")
                        ),
                        ScaleReaderRegistry.getInstance().resolve(AccountId.class),
                        ScaleWriterRegistry.getInstance().resolve(AccountId.class),
                        Blake2B128Concat.getInstance(),
                        AccountId.fromBytes(
                                SS58Codec.decode(
                                                "5FHneW46xGXgs5mUiveU4sbTyGBzmstUspZC92UhjJM694ty")
                                        .getAddress()),
                        HexConverter.toBytes("a6b274250e6753f00a000000").length
                ),
                Arguments.of(
                        new ByteArrayInputStream(
                                HexConverter.toBytes("0xabcdef98765432100123456789abcdefabcdef98765432100123456789abcdef")),
                        ScaleReaderRegistry.getInstance().resolve(BlockHash.class),
                        ScaleWriterRegistry.getInstance().resolve(BlockHash.class),
                        Identity.getInstance(),
                        BlockHash.fromBytes(HexConverter.toBytes("0xabcdef98765432100123456789abcdefabcdef98765432100123456789abcdef")),
                        0
                ),
                Arguments.of(
                        new ByteArrayInputStream(
                                HexConverter.toBytes("0x518366b5b1bc7c99d43593c715fdd31c61141abd04a99fd6822c8558854ccde39a5684e7a56da27d")),
                        ScaleReaderRegistry.getInstance().resolve(BlockHash.class),
                        ScaleWriterRegistry.getInstance().resolve(BlockHash.class),
                        TwoX64Concat.getInstance(),
                        AccountId.fromBytes(
                                SS58Codec.decode(
                                                "5GrwvaEF5zXb26Fz9rcQpDWS57CtERHpNehXCPcNoHGKutQY")
                                        .getAddress()),
                        0
                )
        );
    }

    @ParameterizedTest
    @MethodSource("getTestCasesForGetHash")
    <T> void getHash(T key,
                     ScaleReader<T> reader,
                     ScaleWriter<T> writer,
                     KeyHashingAlgorithm algorithm,
                     String expectedInHex) throws IOException {
        val hasher = KeyHasher.with(writer, reader, algorithm);
        val actual = hasher.getHash(key);

        val expected = HexConverter.toBytes(expectedInHex);
        assertArrayEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("getTestCasesForExtractKey")
    <T> void extractKey(InputStream stream,
                        ScaleReader<T> reader,
                        ScaleWriter<T> writer,
                        KeyHashingAlgorithm algorithm,
                        T expected,
                        int expectedAvailable) throws IOException {
        val hasher = KeyHasher.with(writer, reader, algorithm);
        val actual = hasher.extractKey(stream);

        assertEquals(expected, actual);

        val available = stream.available();
        assertEquals(expectedAvailable, available);
    }
}
