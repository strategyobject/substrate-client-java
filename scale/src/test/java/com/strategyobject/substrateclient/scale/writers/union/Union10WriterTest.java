package com.strategyobject.substrateclient.scale.writers.union;

import com.strategyobject.substrateclient.types.union.Union10;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

class Union10WriterTest {

    @SuppressWarnings("unchecked")
    @Test
    void write() {
        UnionWriterTester.testWrite(new Union10Writer(), new Function[]{
                Union10::withItem0,
                Union10::withItem1,
                Union10::withItem2,
                Union10::withItem3,
                Union10::withItem4,
                Union10::withItem5,
                Union10::withItem6,
                Union10::withItem7,
                Union10::withItem8,
                Union10::withItem9
        });
    }
}