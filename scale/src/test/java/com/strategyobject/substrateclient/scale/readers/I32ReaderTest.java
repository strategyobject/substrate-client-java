package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.common.utils.HexConverter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class I32ReaderTest {
    private final I32Reader i32Reader = new I32Reader();

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"0x00000000,0", "0x45000000,69", "0x00000080,-2147483648", "0xffffff7f,2147483647"})
    void read(String input, int expected) {
        val bytes = HexConverter.toBytes(input);
        val stream = new ByteArrayInputStream(bytes);
        val actual = i32Reader.read(stream);
        assertEquals(expected, actual);
    }
}
