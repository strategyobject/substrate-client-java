package com.strategyobject.substrateclient.scale.writers.union;

import com.strategyobject.substrateclient.common.types.union.Union7;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

class Union7WriterTest {

    @SuppressWarnings("unchecked")
    @Test
    void write() {
        UnionWriterTester.testWrite(new Union7Writer(), new Function[]{
                Union7::withItem0,
                Union7::withItem1,
                Union7::withItem2,
                Union7::withItem3,
                Union7::withItem4,
                Union7::withItem5,
                Union7::withItem6
        });
    }
}