package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.common.convert.HexConverter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class I16WriterTest {
    private final I16Writer i16Writer = new I16Writer();

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"0,0x0000", "69,0x4500", "-32768,0x0080", "32767,0xff7f"})
    void write(short input, String expected) {
        val stream = new ByteArrayOutputStream();
        i16Writer.write(input, stream);
        val actual = HexConverter.toHex(stream.toByteArray());
        assertEquals(expected, actual);
    }
}