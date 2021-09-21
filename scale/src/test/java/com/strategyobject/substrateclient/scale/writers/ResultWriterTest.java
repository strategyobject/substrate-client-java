package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.types.Result;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class ResultWriterTest {

    @SneakyThrows
    @Test
    void writeOk() {
        val resultWriter = new ResultWriter();

        val stream = new ByteArrayOutputStream();
        resultWriter.write(Result.ok(42), stream, new U8Writer(), new VoidWriter());
        val actual = stream.toByteArray();
        assertArrayEquals(new byte[]{0, 42}, actual);
    }

    @SneakyThrows
    @Test
    void writeErr() {
        val resultWriter = new ResultWriter();

        val stream = new ByteArrayOutputStream();
        resultWriter.write(Result.err(false), stream, new VoidWriter(), new BoolWriter());
        val actual = stream.toByteArray();
        assertArrayEquals(new byte[]{1, 0}, actual);
    }
}