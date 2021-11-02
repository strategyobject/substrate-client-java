package com.strategyobject.substrateclient.types.union;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Union2Test {

    @Test
    void getItem0() {
        val expected = false;
        val union = Union2.withItem0(expected);

        Assertions.assertEquals(0, union.getIndex());
        Assertions.assertEquals(expected, union.getItem0());
        Assertions.assertThrows(IllegalStateException.class, union::getItem1);
    }

    @Test
    void getItem1() {
        val expected = 1;
        val union = Union2.withItem1(expected);

        Assertions.assertEquals(1, union.getIndex());
        Assertions.assertThrows(IllegalStateException.class, union::getItem0);
        Assertions.assertEquals(expected, union.getItem1());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    void match() {
        Function[] creators = {Union2::withItem0, Union2::withItem1};
        IntStream.range(0, 2).forEach(expected -> {
            val union = (Union2) creators[expected].apply(expected);

            assertEquals(expected, union.match(Function.identity(), Function.identity()));
        });
    }
}