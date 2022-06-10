package com.strategyobject.substrateclient.scale.readers.union;

import com.strategyobject.substrateclient.common.types.union.Union12;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

class Union12ReaderTest {

    @SuppressWarnings({"rawtypes"})
    @SneakyThrows
    @Test
    void read() {
        UnionReaderTester.testRead(new Union12Reader(), new Function[]{
                x -> ((Union12) x).getItem0(),
                x -> ((Union12) x).getItem1(),
                x -> ((Union12) x).getItem2(),
                x -> ((Union12) x).getItem3(),
                x -> ((Union12) x).getItem4(),
                x -> ((Union12) x).getItem5(),
                x -> ((Union12) x).getItem6(),
                x -> ((Union12) x).getItem7(),
                x -> ((Union12) x).getItem8(),
                x -> ((Union12) x).getItem9(),
                x -> ((Union12) x).getItem10(),
                x -> ((Union12) x).getItem11()
        });
    }
}