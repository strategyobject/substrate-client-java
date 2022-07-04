package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.common.convert.HexConverter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OptionReaderTest {

    @SneakyThrows
    @Test
    void readNone() {
        val optionReader = new OptionReader();

        val bytes = HexConverter.toBytes("0x00");
        val stream = new ByteArrayInputStream(bytes);
        val actual = optionReader.read(stream, new VoidReader());
        assertEquals(Optional.empty(), actual);
    }

    @SneakyThrows
    @Test
    void readSome() {
        val optionReader = new OptionReader();

        val bytes = HexConverter.toBytes("0x0100");
        val stream = new ByteArrayInputStream(bytes);
        val actual = optionReader.read(stream, new I8Reader());
        assertEquals(Optional.of((byte) 0), actual);
    }
}