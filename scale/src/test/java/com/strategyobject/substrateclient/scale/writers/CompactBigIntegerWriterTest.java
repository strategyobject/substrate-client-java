package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.common.utils.HexConverter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CompactBigIntegerWriterTest {
    private final CompactBigIntegerWriter compactBigIntegerWriter = new CompactBigIntegerWriter();

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"0,0x00", "1,0x04", "42,0xa8", "69,0x1501", "1073741823,0xfeffffff", "1073741824,0x0300000040"})
    void write(String input, String expected) {
        val stream = new ByteArrayOutputStream();
        compactBigIntegerWriter.write(new BigInteger(input), stream);
        val actual = HexConverter.toHex(stream.toByteArray());
        assertEquals(expected, actual);
    }
}