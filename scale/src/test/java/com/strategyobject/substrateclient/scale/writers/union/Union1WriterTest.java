package com.strategyobject.substrateclient.scale.writers.union;

import com.strategyobject.substrateclient.scale.writers.I8Writer;
import com.strategyobject.substrateclient.types.union.Union1;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class Union1WriterTest {
    private final Union1Writer unionWriter = new Union1Writer();

    @SneakyThrows
    @Test
    void writeItem0() {
        val stream = new ByteArrayOutputStream();
        unionWriter.write(Union1.withItem0((byte) 1), stream, new I8Writer());
        val actual = stream.toByteArray();
        assertArrayEquals(new byte[]{0, 1}, actual);
    }
}