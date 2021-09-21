package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.common.utils.HexConverter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CompactBigIntegerReaderTest {
    private final CompactBigIntegerReader compactBigIntegerReader = new CompactBigIntegerReader();

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"0x00,0", "0x04,1", "0xa8,42", "0x1501,69", "0xfeffffff,1073741823", "0x0300000040,1073741824"})
    void read(String input, String expected) {
        val bytes = HexConverter.toBytes(input);
        val stream = new ByteArrayInputStream(bytes);
        val actual = compactBigIntegerReader.read(stream);
        assertEquals(new BigInteger(expected), actual);
    }
}
