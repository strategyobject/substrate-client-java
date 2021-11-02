package com.strategyobject.substrateclient.scale.readers.union;

import com.strategyobject.substrateclient.types.union.Union4;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

class Union4ReaderTest {

    @SuppressWarnings({"rawtypes"})
    @SneakyThrows
    @Test
    void read() {
        UnionReaderTester.testRead(new Union4Reader(), new Function[]{
                x -> ((Union4) x).getItem0(),
                x -> ((Union4) x).getItem1(),
                x -> ((Union4) x).getItem2(),
                x -> ((Union4) x).getItem3()
        });
    }
}