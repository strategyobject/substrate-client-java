package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.common.utils.Convert;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class U128WriterTest {
    private final U128Writer u128Writer = new U128Writer();

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({
            "0,0x00000000000000000000000000000000",
            "42,0x2a000000000000000000000000000000",
            "69,0x45000000000000000000000000000000",
            "16777215,0xffffff00000000000000000000000000",
            "340282366920938463463374607431768211455,0xffffffffffffffffffffffffffffffff"
    })
    void write(String input, String expected) {
        val stream = new ByteArrayOutputStream();
        u128Writer.write(new BigInteger(input), stream);
        val actual = Convert.toHex(stream.toByteArray());
        assertEquals(expected, actual);
    }
}