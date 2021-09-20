package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.common.utils.Convert;
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
        val optionReader = new OptionReader<>(null);

        val bytes = Convert.toBytes("0x00");
        val stream = new ByteArrayInputStream(bytes);
        val actual = optionReader.read(stream);
        assertEquals(Optional.empty(), actual);
    }

    @SneakyThrows
    @Test
    void readSome() {
        val optionReader = new OptionReader<>(new I8Reader());

        val bytes = Convert.toBytes("0x0100");
        val stream = new ByteArrayInputStream(bytes);
        val actual = optionReader.read(stream);
        assertEquals(Optional.of((byte) 0), actual);
    }
}