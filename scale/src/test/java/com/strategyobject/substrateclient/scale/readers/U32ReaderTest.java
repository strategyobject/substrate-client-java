package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.common.convert.HexConverter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class U32ReaderTest {
    private final U32Reader u32Reader = new U32Reader();

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"0x00000000,0", "0x2a000000,42", "0x45000000,69", "0xffffff00,16777215", "0xffffffff,4294967295"})
    void read(String input, long expected) {
        val bytes = HexConverter.toBytes(input);
        val stream = new ByteArrayInputStream(bytes);
        val actual = u32Reader.read(stream);
        assertEquals(expected, actual);
    }
}
