package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.common.utils.HexConverter;
import com.strategyobject.substrateclient.types.Result;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResultReaderTest {

    @SneakyThrows
    @Test
    void readOk() {
        val resultReader = new ResultReader();

        val bytes = HexConverter.toBytes("0x002a");
        val stream = new ByteArrayInputStream(bytes);
        val actual = resultReader.read(stream, new U8Reader(), new VoidReader());
        assertEquals(Result.ok(42), actual);
    }

    @SneakyThrows
    @Test
    void readErr() {
        val resultReader = new ResultReader();

        val bytes = HexConverter.toBytes("0x0100");
        val stream = new ByteArrayInputStream(bytes);
        val actual = resultReader.read(stream, new VoidReader(), new BoolReader());
        assertEquals(Result.err(false), actual);
    }
}