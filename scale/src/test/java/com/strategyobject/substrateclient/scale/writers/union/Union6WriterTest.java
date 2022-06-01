package com.strategyobject.substrateclient.scale.writers.union;

import com.strategyobject.substrateclient.common.types.union.Union6;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

class Union6WriterTest {

    @SuppressWarnings("unchecked")
    @Test
    void write() {
        UnionWriterTester.testWrite(new Union6Writer(), new Function[]{
                Union6::withItem0,
                Union6::withItem1,
                Union6::withItem2,
                Union6::withItem3,
                Union6::withItem4,
                Union6::withItem5
        });
    }
}