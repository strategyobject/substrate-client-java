package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.common.utils.HexConverter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VecReaderTest {

    @SneakyThrows
    @Test
    void read() {
        val listReader = new VecReader();

        val bytes = HexConverter.toBytes("0x18040008000f00100017002a00");
        val stream = new ByteArrayInputStream(bytes);
        val actual = listReader.read(stream, new U16Reader());

        assertEquals(Arrays.asList(4, 8, 15, 16, 23, 42), actual);
    }

    @SneakyThrows
    @Test
    void readEmpty() {
        val listReader = new VecReader();

        val bytes = HexConverter.toBytes("0x00");
        val stream = new ByteArrayInputStream(bytes);
        val actual = listReader.read(stream, new U16Reader());

        assertEquals(new ArrayList<>(), actual);
    }
}