package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.common.convert.HexConverter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class U128ReaderTest {
    private final U128Reader u128Reader = new U128Reader();

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({
            "0x00000000000000000000000000000000,0",
            "0x2a000000000000000000000000000000,42",
            "0x45000000000000000000000000000000,69",
            "0xffffff00000000000000000000000000,16777215",
            "0xffffffffffffffffffffffffffffffff,340282366920938463463374607431768211455"
    })
    void read(String input, String expected) {
        val bytes = HexConverter.toBytes(input);
        val stream = new ByteArrayInputStream(bytes);
        val actual = u128Reader.read(stream);
        assertEquals(new BigInteger(expected), actual);
    }
}
