package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.common.convert.HexConverter;
import com.strategyobject.substrateclient.common.types.tuple.Pair;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class PairWriterTest {
    private final PairWriter pairWriter = new PairWriter();

    @Test
    void write() throws IOException {
        val stream = new ByteArrayOutputStream();
        pairWriter.write(Pair.of(true, "test"), stream, new BoolWriter(), new StringWriter());
        val actual = stream.toByteArray();
        assertArrayEquals(HexConverter.toBytes("0x011074657374"), actual);
    }
}