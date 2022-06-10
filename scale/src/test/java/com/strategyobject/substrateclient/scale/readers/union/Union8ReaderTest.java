package com.strategyobject.substrateclient.scale.readers.union;

import com.strategyobject.substrateclient.common.types.union.Union8;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

class Union8ReaderTest {

    @SuppressWarnings({"rawtypes"})
    @SneakyThrows
    @Test
    void read() {
        UnionReaderTester.testRead(new Union8Reader(), new Function[]{
                x -> ((Union8) x).getItem0(),
                x -> ((Union8) x).getItem1(),
                x -> ((Union8) x).getItem2(),
                x -> ((Union8) x).getItem3(),
                x -> ((Union8) x).getItem4(),
                x -> ((Union8) x).getItem5(),
                x -> ((Union8) x).getItem6(),
                x -> ((Union8) x).getItem7()
        });
    }
}