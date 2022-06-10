package com.strategyobject.substrateclient.scale.writers.union;

import com.strategyobject.substrateclient.common.types.union.Union;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.scale.writers.I8Writer;
import com.strategyobject.substrateclient.scale.writers.VoidWriter;
import lombok.SneakyThrows;
import lombok.val;
import lombok.var;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class UnionWriterTester {

    @SneakyThrows
    static <T extends Union> void testWrite(ScaleWriter<T> writer, Function<Object, T>[] creators) {
        for (var i = 0; i < creators.length; i++) {
            val stream = new ByteArrayOutputStream();
            val writers = new ScaleWriter<?>[creators.length];
            Arrays.fill(writers, new VoidWriter());
            writers[i] = new I8Writer();
            val union = creators[i].apply((byte) 1);
            writer.write(union, stream, writers);
            val actual = stream.toByteArray();

            assertArrayEquals(new byte[]{(byte) i, 1}, actual);
        }
    }
}
