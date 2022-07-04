package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.common.convert.HexConverter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class I64WriterTest {
    private final I64Writer i64Writer = new I64Writer();

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({
            "0,0x0000000000000000",
            "69,0x4500000000000000",
            "-9223372036854775808,0x0000000000000080",
            "9223372036854775807,0xffffffffffffff7f"
    })
    void write(long input, String expected) {
        val stream = new ByteArrayOutputStream();
        i64Writer.write(input, stream);
        val actual = HexConverter.toHex(stream.toByteArray());
        assertEquals(expected, actual);
    }
}