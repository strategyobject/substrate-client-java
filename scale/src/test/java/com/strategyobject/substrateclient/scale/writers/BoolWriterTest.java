package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.common.convert.HexConverter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoolWriterTest {
    private final BoolWriter boolWriter = new BoolWriter();

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"false,0x00", "true,0x01"})
    void write(boolean input, String expected) {
        val stream = new ByteArrayOutputStream();
        boolWriter.write(input, stream);
        val actual = HexConverter.toHex(stream.toByteArray());
        assertEquals(expected, actual);
    }
}