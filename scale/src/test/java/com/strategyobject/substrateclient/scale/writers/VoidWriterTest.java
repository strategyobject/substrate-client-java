package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.common.convert.HexConverter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VoidWriterTest {

    private final VoidWriter voidWriter = new VoidWriter();

    @SneakyThrows
    @Test
    void write() {
        val stream = new ByteArrayOutputStream();
        voidWriter.write(null, stream);
        val actual = HexConverter.toHex(stream.toByteArray());

        assertEquals("0x", actual);
    }
}