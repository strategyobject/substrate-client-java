package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.common.convert.HexConverter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class U32WriterTest {
    private final U32Writer u32Writer = new U32Writer();

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"0,0x00000000", "42,0x2a000000", "69,0x45000000", "16777215,0xffffff00", "4294967295,0xffffffff"})
    void write(long input, String expected) {
        val stream = new ByteArrayOutputStream();
        u32Writer.write(input, stream);
        val actual = HexConverter.toHex(stream.toByteArray());
        assertEquals(expected, actual);
    }
}