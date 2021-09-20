package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.common.utils.Convert;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class I16ReaderTest {
    private final I16Reader i16Reader = new I16Reader();

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"0x0000,0", "0x4500,69", "0x0080,-32768", "0xff7f,32767"})
    void read(String input, short expected) {
        val bytes = Convert.toBytes(input);
        val stream = new ByteArrayInputStream(bytes);
        val actual = i16Reader.read(stream);
        assertEquals(expected, actual);
    }
}
