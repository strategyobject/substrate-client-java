package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.common.utils.Convert;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CompactIntegerReaderTest {
    private final CompactIntegerReader compactIntegerReader = new CompactIntegerReader();

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"0x00,0", "0x04,1", "0xa8,42", "0x1501,69", "0xfeffffff,1073741823"})
    void read(String input, int expected) {
        val bytes = Convert.toBytes(input);
        val stream = new ByteArrayInputStream(bytes);
        val actual = compactIntegerReader.read(stream);
        assertEquals(expected, actual);

    }

    @Test
    void readBigIntegerUnsupported() {
        val bytes = Convert.toBytes("0x0300000040");
        val stream = new ByteArrayInputStream(bytes);
        assertThrows(UnsupportedOperationException.class, () -> compactIntegerReader.read(stream));
    }
}