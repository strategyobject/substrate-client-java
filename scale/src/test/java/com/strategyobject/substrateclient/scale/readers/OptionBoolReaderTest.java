package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.common.utils.HexConverter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OptionBoolReaderTest {
    private final OptionBoolReader optionBoolReader = new OptionBoolReader();

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"0x00,Optional.empty", "0x01,Optional[true]", "0x02,Optional[false]"})
    void read(String input, String expected) {
        val bytes = HexConverter.toBytes(input);
        val stream = new ByteArrayInputStream(bytes);
        val actual = optionBoolReader.read(stream);
        assertEquals(expected, actual.toString());
    }
}