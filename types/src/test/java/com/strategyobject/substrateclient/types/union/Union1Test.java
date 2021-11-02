package com.strategyobject.substrateclient.types.union;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Union1Test {

    @Test
    void getItem0() {
        val expected = false;
        val union = Union1.withItem0(expected);

        assertEquals(0, union.getIndex());
        assertEquals(expected, union.getItem0());
    }

    @Test
    void match() {
        val expected = 0;
        val union = Union1.withItem0(expected);

        assertEquals(expected, union.match(Function.identity()));
    }
}