package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.common.utils.Convert;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class U16ReaderTest {
    private final U16Reader u16Reader = new U16Reader();

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"0x0000,0", "0x2a00,42", "0x4500,69", "0xffff,65535"})
    void read(String input, int expected) {
        val bytes = Convert.toBytes(input);
        val stream = new ByteArrayInputStream(bytes);
        val actual = u16Reader.read(stream);
        assertEquals(expected, actual);
    }
}
