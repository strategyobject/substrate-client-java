package com.strategyobject.substrateclient.scale.writers.union;

import com.strategyobject.substrateclient.types.union.Union9;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

class Union9WriterTest {

    @SuppressWarnings("unchecked")
    @Test
    void write() {
        UnionWriterTester.testWrite(new Union9Writer(), new Function[]{
                Union9::withItem0,
                Union9::withItem1,
                Union9::withItem2,
                Union9::withItem3,
                Union9::withItem4,
                Union9::withItem5,
                Union9::withItem6,
                Union9::withItem7,
                Union9::withItem8
        });
    }
}