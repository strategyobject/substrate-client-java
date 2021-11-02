package com.strategyobject.substrateclient.scale.writers.union;

import com.strategyobject.substrateclient.types.union.Union3;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

class Union3WriterTest {

    @SuppressWarnings("unchecked")
    @Test
    void write() {
        UnionWriterTester.testWrite(new Union3Writer(), new Function[]{
                Union3::withItem0,
                Union3::withItem1,
                Union3::withItem2
        });
    }
}