package com.strategyobject.substrateclient.scale.writers;

import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class OptionBoolWriterTest {
    private final OptionBoolWriter optionBoolWriter = new OptionBoolWriter();

    @SneakyThrows
    @Test
    void writeNone() {
        val stream = new ByteArrayOutputStream();
        optionBoolWriter.write(Optional.empty(), stream);
        val actual = stream.toByteArray();
        assertArrayEquals(new byte[]{0}, actual);
    }

    @SneakyThrows
    @Test
    void writeSomeTrue() {
        val stream = new ByteArrayOutputStream();
        optionBoolWriter.write(Optional.of(true), stream);
        val actual = stream.toByteArray();
        assertArrayEquals(new byte[]{1}, actual);
    }

    @SneakyThrows
    @Test
    void writeSomeFalse() {
        val stream = new ByteArrayOutputStream();
        optionBoolWriter.write(Optional.of(false), stream);
        val actual = stream.toByteArray();
        assertArrayEquals(new byte[]{2}, actual);
    }
}