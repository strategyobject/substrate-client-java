package com.strategyobject.substrateclient.scale.writers;

import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class OptionWriterTest {
    private final OptionWriter<Byte> optionWriter = new OptionWriter<>();

    @SneakyThrows
    @Test
    void writeNone() {
        val stream = new ByteArrayOutputStream();
        optionWriter.write(Optional.empty(), stream, new I8Writer());
        val actual = stream.toByteArray();
        assertArrayEquals(new byte[]{0}, actual);
    }

    @SneakyThrows
    @Test
    void writeSomeTrue() {
        val stream = new ByteArrayOutputStream();
        optionWriter.write(Optional.of((byte) 0), stream, new I8Writer());
        val actual = stream.toByteArray();
        assertArrayEquals(new byte[]{1, 0}, actual);
    }
}