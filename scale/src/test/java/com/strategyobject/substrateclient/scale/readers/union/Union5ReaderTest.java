package com.strategyobject.substrateclient.scale.readers.union;

import com.strategyobject.substrateclient.types.union.Union5;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

class Union5ReaderTest {

    @SuppressWarnings({"rawtypes"})
    @SneakyThrows
    @Test
    void read() {
        UnionReaderTester.testRead(new Union5Reader(), new Function[]{
                x -> ((Union5) x).getItem0(),
                x -> ((Union5) x).getItem1(),
                x -> ((Union5) x).getItem2(),
                x -> ((Union5) x).getItem3(),
                x -> ((Union5) x).getItem4()
        });
    }
}