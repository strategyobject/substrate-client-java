package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.common.utils.HexConverter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringWriterTest {
    private final StringWriter stringWriter = new StringWriter();

    @SneakyThrows
    @Test
    void write() {
        val stream = new ByteArrayOutputStream();
        stringWriter.write("test", stream);
        val actual = HexConverter.toHex(stream.toByteArray());

        assertEquals("0x1074657374", actual);
    }

    @SneakyThrows
    @Test
    void writeEmpty() {
        val stream = new ByteArrayOutputStream();
        stringWriter.write("", stream);
        val actual = HexConverter.toHex(stream.toByteArray());

        assertEquals("0x00", actual);
    }
}