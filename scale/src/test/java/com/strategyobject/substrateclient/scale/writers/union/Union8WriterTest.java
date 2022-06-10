package com.strategyobject.substrateclient.scale.writers.union;

import com.strategyobject.substrateclient.common.types.union.Union8;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

class Union8WriterTest {

    @SuppressWarnings("unchecked")
    @Test
    void write() {
        UnionWriterTester.testWrite(new Union8Writer(), new Function[]{
                Union8::withItem0,
                Union8::withItem1,
                Union8::withItem2,
                Union8::withItem3,
                Union8::withItem4,
                Union8::withItem5,
                Union8::withItem6,
                Union8::withItem7
        });
    }
}