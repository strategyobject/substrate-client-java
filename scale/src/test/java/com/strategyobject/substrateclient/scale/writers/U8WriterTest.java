package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.common.convert.HexConverter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class U8WriterTest {
    private final U8Writer u8Writer = new U8Writer();

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"0,0x00", "42,0x2a", "69,0x45", "255,0xff"})
    void write(int input, String expected) {
        val stream = new ByteArrayOutputStream();
        u8Writer.write(input, stream);
        val actual = HexConverter.toHex(stream.toByteArray());
        assertEquals(expected, actual);
    }
}