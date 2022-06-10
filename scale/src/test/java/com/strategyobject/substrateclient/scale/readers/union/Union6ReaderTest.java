package com.strategyobject.substrateclient.scale.readers.union;

import com.strategyobject.substrateclient.common.types.union.Union6;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

class Union6ReaderTest {

    @SuppressWarnings({"rawtypes"})
    @SneakyThrows
    @Test
    void read() {
        UnionReaderTester.testRead(new Union6Reader(), new Function[]{
                x -> ((Union6) x).getItem0(),
                x -> ((Union6) x).getItem1(),
                x -> ((Union6) x).getItem2(),
                x -> ((Union6) x).getItem3(),
                x -> ((Union6) x).getItem4(),
                x -> ((Union6) x).getItem5()
        });
    }
}