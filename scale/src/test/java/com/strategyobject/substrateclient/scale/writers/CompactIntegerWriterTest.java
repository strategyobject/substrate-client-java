package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.common.utils.Convert;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CompactIntegerWriterTest {
    private final CompactIntegerWriter compactIntegerWriter = new CompactIntegerWriter();

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"0,0x00", "1,0x04", "42,0xa8", "69,0x1501", "1073741823,0xfeffffff"})
    void write(int input, String expected) {
        val stream = new ByteArrayOutputStream();
        compactIntegerWriter.write(input, stream);
        val actual = Convert.toHex(stream.toByteArray());
        assertEquals(expected, actual);
    }

    @Test
    void writeBigIntegerUnsupported() {
        val stream = new ByteArrayOutputStream();
        assertThrows(UnsupportedOperationException.class, () -> compactIntegerWriter.write(1073741824, stream));
    }
}