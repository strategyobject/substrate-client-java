package com.strategyobject.substrateclient.scale.writers.union;

import com.strategyobject.substrateclient.common.types.union.Union4;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

class Union4WriterTest {

    @SuppressWarnings("unchecked")
    @Test
    void write() {
        UnionWriterTester.testWrite(new Union4Writer(), new Function[]{
                Union4::withItem0,
                Union4::withItem1,
                Union4::withItem2,
                Union4::withItem3
        });
    }
}