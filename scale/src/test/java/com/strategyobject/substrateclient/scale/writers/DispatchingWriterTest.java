package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.common.convert.HexConverter;
import com.strategyobject.substrateclient.scale.ScaleDispatch;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import com.strategyobject.substrateclient.scale.substitutes.TestDispatch;
import com.strategyobject.substrateclient.scale.substitutes.TestDispatchWriter;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DispatchingWriterTest {
    private final ScaleWriterRegistry registry = new ScaleWriterRegistry() {{
        register(new TestDispatchWriter(this), TestDispatch.class);
    }};

    @SuppressWarnings("unchecked")
    @Test
    void write() throws IOException {
        val writer = (ScaleWriter<TestDispatch>) registry.resolve(ScaleDispatch.class);

        val stream = new ByteArrayOutputStream();
        writer.write(new TestDispatch("test"), stream);
        val actual = HexConverter.toHex(stream.toByteArray());

        assertEquals("0x1074657374", actual);
    }
}