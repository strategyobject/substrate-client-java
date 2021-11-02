package com.strategyobject.substrateclient.scale.readers.union;

import com.strategyobject.substrateclient.common.utils.HexConverter;
import com.strategyobject.substrateclient.scale.readers.BoolReader;
import com.strategyobject.substrateclient.scale.readers.U8Reader;
import com.strategyobject.substrateclient.scale.readers.VoidReader;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Union3ReaderTest {
    private final Union3Reader unionReader = new Union3Reader();

    @SneakyThrows
    @Test
    void readItem0() {
        val bytes = HexConverter.toBytes("0x002a");
        val stream = new ByteArrayInputStream(bytes);
        val actual = unionReader.read(stream, new U8Reader(), new VoidReader(), new VoidReader());
        assertEquals(42, actual.getItem0());
    }

    @SneakyThrows
    @Test
    void readItem1() {
        val bytes = HexConverter.toBytes("0x0101");
        val stream = new ByteArrayInputStream(bytes);
        val actual = unionReader.read(stream, new VoidReader(), new BoolReader(), new VoidReader());
        assertEquals(true, actual.getItem1());
    }

    @SneakyThrows
    @Test
    void readItem2() {
        val bytes = HexConverter.toBytes("0x0201");
        val stream = new ByteArrayInputStream(bytes);
        val actual = unionReader.read(stream, new VoidReader(), new VoidReader(), new U8Reader());
        assertEquals(1, actual.getItem2());
    }
}