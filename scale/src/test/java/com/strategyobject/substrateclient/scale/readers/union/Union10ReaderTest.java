package com.strategyobject.substrateclient.scale.readers.union;

import com.strategyobject.substrateclient.types.union.Union10;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

class Union10ReaderTest {

    @SuppressWarnings({"rawtypes"})
    @SneakyThrows
    @Test
    void read() {
        UnionReaderTester.testRead(new Union10Reader(), new Function[]{
                x -> ((Union10) x).getItem0(),
                x -> ((Union10) x).getItem1(),
                x -> ((Union10) x).getItem2(),
                x -> ((Union10) x).getItem3(),
                x -> ((Union10) x).getItem4(),
                x -> ((Union10) x).getItem5(),
                x -> ((Union10) x).getItem6(),
                x -> ((Union10) x).getItem7(),
                x -> ((Union10) x).getItem8(),
                x -> ((Union10) x).getItem9()
        });
    }
}