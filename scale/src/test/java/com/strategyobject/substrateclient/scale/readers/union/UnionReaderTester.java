package com.strategyobject.substrateclient.scale.readers.union;

import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.readers.U8Reader;
import com.strategyobject.substrateclient.scale.readers.VoidReader;
import com.strategyobject.substrateclient.types.union.Union;
import lombok.SneakyThrows;
import lombok.val;
import lombok.var;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnionReaderTester {

    @SneakyThrows
    @SuppressWarnings({"rawtypes", "unchecked"})
    static <T extends Union> void testRead(ScaleReader<T> reader, Function[] getters) {
        for (var i = 0; i < getters.length; i++) {
            byte[] bytes = {(byte) i, 1};
            val stream = new ByteArrayInputStream(bytes);
            val readers = new ScaleReader<?>[getters.length];
            Arrays.fill(readers, new VoidReader());
            readers[i] = new U8Reader();
            val actual = reader.read(stream, readers);

            assertEquals(1, getters[i].apply(actual));
        }
    }
}
