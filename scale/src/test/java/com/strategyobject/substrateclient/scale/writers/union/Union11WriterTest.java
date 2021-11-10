package com.strategyobject.substrateclient.scale.writers.union;

import com.strategyobject.substrateclient.types.union.Union11;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

class Union11WriterTest {

    @SuppressWarnings("unchecked")
    @Test
    void write() {
        UnionWriterTester.testWrite(new Union11Writer(), new Function[]{
                Union11::withItem0,
                Union11::withItem1,
                Union11::withItem2,
                Union11::withItem3,
                Union11::withItem4,
                Union11::withItem5,
                Union11::withItem6,
                Union11::withItem7,
                Union11::withItem8,
                Union11::withItem9,
                Union11::withItem10
        });
    }
}