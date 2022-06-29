package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.common.convert.HexConverter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class I8WriterTest {
    private final I8Writer i8Writer = new I8Writer();

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"0,0x00", "69,0x45", "-128,0x80", "127,0x7f"})
    void write(byte input, String expected) {
        val stream = new ByteArrayOutputStream();
        i8Writer.write(input, stream);
        val actual = HexConverter.toHex(stream.toByteArray());
        assertEquals(expected, actual);
    }
}