package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.common.utils.Convert;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringReaderTest {

    @SneakyThrows
    @Test
    void read() {
        val stringReader = new StringReader();

        val bytes = Convert.toBytes("0x1074657374");
        val stream = new ByteArrayInputStream(bytes);
        val actual = stringReader.read(stream);

        assertEquals("test", actual);
    }

    @SneakyThrows
    @Test
    void readEmpty() {
        val stringReader = new StringReader();

        val bytes = Convert.toBytes("0x00");
        val stream = new ByteArrayInputStream(bytes);
        val actual = stringReader.read(stream);

        assertEquals("", actual);
    }
}