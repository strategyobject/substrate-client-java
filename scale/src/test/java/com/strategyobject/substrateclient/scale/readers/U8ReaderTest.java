package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.common.convert.HexConverter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class U8ReaderTest {
    private final U8Reader u8Reader = new U8Reader();

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"0x00,0", "0x2a,42", "0x45,69", "0xff,255"})
    void read(String input, int expected) {
        val bytes = HexConverter.toBytes(input);
        val stream = new ByteArrayInputStream(bytes);
        val actual = u8Reader.read(stream);
        assertEquals(expected, actual);
    }
}