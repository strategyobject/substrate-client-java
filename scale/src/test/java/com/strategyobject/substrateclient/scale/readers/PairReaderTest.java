package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.common.convert.HexConverter;
import com.strategyobject.substrateclient.common.types.tuple.Pair;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PairReaderTest {
    private final PairReader pairReader = new PairReader();

    @Test
    void read() throws IOException {
        val bytes = HexConverter.toBytes("0x011074657374");
        val stream = new ByteArrayInputStream(bytes);
        val actual = pairReader.read(stream, new BoolReader(), new StringReader());
        assertEquals(Pair.of(true, "test"), actual);
    }
}