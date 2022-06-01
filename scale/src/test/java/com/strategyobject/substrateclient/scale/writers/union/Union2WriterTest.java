package com.strategyobject.substrateclient.scale.writers.union;

import com.strategyobject.substrateclient.common.types.union.Union2;
import com.strategyobject.substrateclient.scale.writers.I8Writer;
import com.strategyobject.substrateclient.scale.writers.VoidWriter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class Union2WriterTest {
    private final Union2Writer unionWriter = new Union2Writer();

    @SneakyThrows
    @Test
    void writeItem0() {
        val stream = new ByteArrayOutputStream();
        unionWriter.write(Union2.withItem0((byte) 1), stream, new I8Writer(), new VoidWriter());
        val actual = stream.toByteArray();
        assertArrayEquals(new byte[]{0, 1}, actual);
    }

    @SneakyThrows
    @Test
    void writeItem1() {
        val stream = new ByteArrayOutputStream();
        unionWriter.write(Union2.withItem1((byte) 1), stream, new VoidWriter(), new I8Writer());
        val actual = stream.toByteArray();
        assertArrayEquals(new byte[]{1, 1}, actual);
    }
}