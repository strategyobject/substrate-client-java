package com.strategyobject.substrateclient.scale.writers.union;

import com.strategyobject.substrateclient.types.union.Union12;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

class Union12WriterTest {

    @SuppressWarnings("unchecked")
    @Test
    void write() {
        UnionWriterTester.testWrite(new Union12Writer(), new Function[]{
                Union12::withItem0,
                Union12::withItem1,
                Union12::withItem2,
                Union12::withItem3,
                Union12::withItem4,
                Union12::withItem5,
                Union12::withItem6,
                Union12::withItem7,
                Union12::withItem8,
                Union12::withItem9,
                Union12::withItem10,
                Union12::withItem11
        });
    }
}