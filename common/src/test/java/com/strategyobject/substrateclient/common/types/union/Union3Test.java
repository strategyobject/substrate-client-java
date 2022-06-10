package com.strategyobject.substrateclient.common.types.union;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Union3Test {

    @Test
    void getItem0() {
        val expected = false;
        val union = Union3.withItem0(expected);

        assertEquals(0, union.getIndex());
        assertEquals(expected, union.getItem0());
        Assertions.assertThrows(IllegalStateException.class, union::getItem1);
        Assertions.assertThrows(IllegalStateException.class, union::getItem2);
    }

    @Test
    void getItem1() {
        val expected = 1;
        val union = Union3.withItem1(expected);

        assertEquals(1, union.getIndex());
        Assertions.assertThrows(IllegalStateException.class, union::getItem0);
        assertEquals(expected, union.getItem1());
        Assertions.assertThrows(IllegalStateException.class, union::getItem2);
    }

    @Test
    void getItem2() {
        val expected = "2";
        val union = Union3.withItem2(expected);

        assertEquals(2, union.getIndex());
        Assertions.assertThrows(IllegalStateException.class, union::getItem0);
        Assertions.assertThrows(IllegalStateException.class, union::getItem1);
        assertEquals(expected, union.getItem2());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    void match() {
        Function[] creators = {
                Union3::withItem0,
                Union3::withItem1,
                Union3::withItem2
        };
        
        IntStream.range(0, 3).forEach(expected -> {
            val union = (Union3) creators[expected].apply(expected);

            assertEquals(expected,
                    union.match(
                            Function.identity(),
                            Function.identity(),
                            Function.identity()));
        });
    }
}