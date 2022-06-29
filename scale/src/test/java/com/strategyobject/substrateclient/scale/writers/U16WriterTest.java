package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.common.convert.HexConverter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class U16WriterTest {
    private final U16Writer u16Writer = new U16Writer();

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"0,0x0000", "42,0x2a00", "69,0x4500", "65535,0xffff"})
    void write(int input, String expected) {
        val stream = new ByteArrayOutputStream();
        u16Writer.write(input, stream);
        val actual = HexConverter.toHex(stream.toByteArray());
        assertEquals(expected, actual);
    }
}