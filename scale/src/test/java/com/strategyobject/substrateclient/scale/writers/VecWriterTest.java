package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.common.convert.HexConverter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VecWriterTest {
    @SneakyThrows
    @Test
    void write() {
        val listWriter = new VecWriter();

        val stream = new ByteArrayOutputStream();
        listWriter.write(Arrays.asList(4, 8, 15, 16, 23, 42), stream, new U16Writer());
        val actual = HexConverter.toHex(stream.toByteArray());

        assertEquals("0x18040008000f00100017002a00", actual);
    }

    @SneakyThrows
    @Test
    void writeEmpty() {
        val listWriter = new VecWriter();

        val stream = new ByteArrayOutputStream();
        listWriter.write(new ArrayList<>(), stream, new U16Writer());
        val actual = HexConverter.toHex(stream.toByteArray());

        assertEquals("0x00", actual);
    }
}
