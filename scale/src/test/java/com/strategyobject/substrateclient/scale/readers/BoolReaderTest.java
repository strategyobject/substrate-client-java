package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.common.utils.HexConverter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoolReaderTest {
    private final BoolReader boolReader = new BoolReader();

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"0x00,false", "0x01,true"})
    void read(String input, Boolean expected) {
        val bytes = HexConverter.toBytes(input);
        val stream = new ByteArrayInputStream(bytes);
        val actual = boolReader.read(stream);
        assertEquals(expected, actual);
    }
}