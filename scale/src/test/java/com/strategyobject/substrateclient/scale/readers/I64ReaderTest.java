package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.common.utils.HexConverter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class I64ReaderTest {
    private final I64Reader i64Reader = new I64Reader();

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({
            "0x0000000000000000,0",
            "0x4500000000000000,69",
            "0x0000000000000080,-9223372036854775808",
            "0xffffffffffffff7f,9223372036854775807"
    })
    void read(String input, long expected) {
        val bytes = HexConverter.toBytes(input);
        val stream = new ByteArrayInputStream(bytes);
        val actual = i64Reader.read(stream);
        assertEquals(expected, actual);
    }
}
