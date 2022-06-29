package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.common.convert.HexConverter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class I32WriterTest {
    private final I32Writer i32Writer = new I32Writer();

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"0,0x00000000", "69,0x45000000", "-2147483648,0x00000080", "2147483647,0xffffff7f"})
    void write(int input, String expected) {
        val stream = new ByteArrayOutputStream();
        i32Writer.write(input, stream);
        val actual = HexConverter.toHex(stream.toByteArray());
        assertEquals(expected, actual);
    }
}