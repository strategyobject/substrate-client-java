package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.common.utils.HexConverter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class I8ReaderTest {
    private final I8Reader i8Reader = new I8Reader();

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"0x00,0", "0x45,69", "0x80,-128", "0x7f,127"})
    void read(String input, byte expected) {
        val bytes = HexConverter.toBytes(input);
        val stream = new ByteArrayInputStream(bytes);
        val actual = i8Reader.read(stream);
        assertEquals(expected, actual);
    }
}