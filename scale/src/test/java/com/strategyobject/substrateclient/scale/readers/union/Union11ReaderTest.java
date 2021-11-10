package com.strategyobject.substrateclient.scale.readers.union;

import com.strategyobject.substrateclient.types.union.Union11;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

class Union11ReaderTest {

    @SuppressWarnings({"rawtypes"})
    @SneakyThrows
    @Test
    void read() {
        UnionReaderTester.testRead(new Union11Reader(), new Function[]{
                x -> ((Union11) x).getItem0(),
                x -> ((Union11) x).getItem1(),
                x -> ((Union11) x).getItem2(),
                x -> ((Union11) x).getItem3(),
                x -> ((Union11) x).getItem4(),
                x -> ((Union11) x).getItem5(),
                x -> ((Union11) x).getItem6(),
                x -> ((Union11) x).getItem7(),
                x -> ((Union11) x).getItem8(),
                x -> ((Union11) x).getItem9(),
                x -> ((Union11) x).getItem10()
        });
    }
}