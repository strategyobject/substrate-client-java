package com.strategyobject.substrateclient.scale.readers.union;

import com.strategyobject.substrateclient.common.utils.HexConverter;
import com.strategyobject.substrateclient.scale.readers.U8Reader;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Union1ReaderTest {
    private final Union1Reader unionReader = new Union1Reader();

    @SneakyThrows
    @Test
    void readItem0() {
        val bytes = HexConverter.toBytes("0x002a");
        val stream = new ByteArrayInputStream(bytes);
        val actual = unionReader.read(stream, new U8Reader());
        assertEquals(42, actual.getItem0());
    }
}