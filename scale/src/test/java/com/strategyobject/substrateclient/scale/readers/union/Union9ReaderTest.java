package com.strategyobject.substrateclient.scale.readers.union;

import com.strategyobject.substrateclient.types.union.Union9;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

class Union9ReaderTest {

    @SuppressWarnings({"rawtypes"})
    @SneakyThrows
    @Test
    void read() {
        UnionReaderTester.testRead(new Union9Reader(), new Function[]{
                x -> ((Union9) x).getItem0(),
                x -> ((Union9) x).getItem1(),
                x -> ((Union9) x).getItem2(),
                x -> ((Union9) x).getItem3(),
                x -> ((Union9) x).getItem4(),
                x -> ((Union9) x).getItem5(),
                x -> ((Union9) x).getItem6(),
                x -> ((Union9) x).getItem7(),
                x -> ((Union9) x).getItem8()
        });
    }
}