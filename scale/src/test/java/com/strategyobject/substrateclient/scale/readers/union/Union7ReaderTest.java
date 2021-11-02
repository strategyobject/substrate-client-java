package com.strategyobject.substrateclient.scale.readers.union;

import com.strategyobject.substrateclient.types.union.Union7;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

class Union7ReaderTest {

    @SuppressWarnings({"rawtypes"})
    @SneakyThrows
    @Test
    void read() {
        UnionReaderTester.testRead(new Union7Reader(), new Function[]{
                x -> ((Union7) x).getItem0(),
                x -> ((Union7) x).getItem1(),
                x -> ((Union7) x).getItem2(),
                x -> ((Union7) x).getItem3(),
                x -> ((Union7) x).getItem4(),
                x -> ((Union7) x).getItem5(),
                x -> ((Union7) x).getItem6()
        });
    }
}