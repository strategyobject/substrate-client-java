package com.strategyobject.substrateclient.scale.writers.union;

import com.strategyobject.substrateclient.common.types.union.Union5;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

class Union5WriterTest {

    @SuppressWarnings("unchecked")
    @Test
    void write() {
        UnionWriterTester.testWrite(new Union5Writer(), new Function[]{
                Union5::withItem0,
                Union5::withItem1,
                Union5::withItem2,
                Union5::withItem3,
                Union5::withItem4
        });
    }
}