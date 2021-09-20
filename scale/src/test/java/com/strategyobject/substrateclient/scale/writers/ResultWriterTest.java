package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.scale.Result;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class ResultWriterTest {

    @SneakyThrows
    @Test
    void writeOk() {
        val resultWriter = new ResultWriter<>(new U8Writer(), null);

        val stream = new ByteArrayOutputStream();
        resultWriter.write(Result.ok(42), stream);
        val actual = stream.toByteArray();
        assertArrayEquals(new byte[]{0, 42}, actual);
    }

    @SneakyThrows
    @Test
    void writeErr() {
        val resultWriter = new ResultWriter<>(null, new BoolWriter());

        val stream = new ByteArrayOutputStream();
        resultWriter.write(Result.err(false), stream);
        val actual = stream.toByteArray();
        assertArrayEquals(new byte[]{1, 0}, actual);
    }
}