package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.common.convert.HexConverter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class U64ReaderTest {
    private final U64Reader u64Reader = new U64Reader();

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({
            "0x0000000000000000,0",
            "0x2a00000000000000,42",
            "0x4500000000000000,69",
            "0xffffff0000000000,16777215",
            "0xffffffffffffffff,18446744073709551615"
    })
    void read(String input, String expected) {
        val bytes = HexConverter.toBytes(input);
        val stream = new ByteArrayInputStream(bytes);
        val actual = u64Reader.read(stream);
        assertEquals(new BigInteger(expected), actual);
    }
}
