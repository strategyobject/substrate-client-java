package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.common.utils.Convert;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class U64WriterTest {
    private final U64Writer u64Writer = new U64Writer();

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({
            "0,0x0000000000000000",
            "42,0x2a00000000000000",
            "69,0x4500000000000000",
            "16777215,0xffffff0000000000",
            "18446744073709551615,0xffffffffffffffff"
    })
    void write(String input, String expected) {
        val stream = new ByteArrayOutputStream();
        u64Writer.write(new BigInteger(input), stream);
        val actual = Convert.toHex(stream.toByteArray());
        assertEquals(expected, actual);
    }
}