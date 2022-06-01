package com.strategyobject.substrateclient.common.types.union;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Union6Test {

    @Test
    void getItem0() {
        val expected = false;
        val union = Union6.withItem0(expected);

        assertEquals(0, union.getIndex());
        assertEquals(expected, union.getItem0());
        Assertions.assertThrows(IllegalStateException.class, union::getItem1);
        Assertions.assertThrows(IllegalStateException.class, union::getItem2);
        Assertions.assertThrows(IllegalStateException.class, union::getItem3);
        Assertions.assertThrows(IllegalStateException.class, union::getItem4);
        Assertions.assertThrows(IllegalStateException.class, union::getItem5);
    }

    @Test
    void getItem1() {
        val expected = 1;
        val union = Union6.withItem1(expected);

        assertEquals(1, union.getIndex());
        Assertions.assertThrows(IllegalStateException.class, union::getItem0);
        assertEquals(expected, union.getItem1());
        Assertions.assertThrows(IllegalStateException.class, union::getItem2);
        Assertions.assertThrows(IllegalStateException.class, union::getItem3);
        Assertions.assertThrows(IllegalStateException.class, union::getItem4);
        Assertions.assertThrows(IllegalStateException.class, union::getItem5);
    }

    @Test
    void getItem2() {
        val expected = "2";
        val union = Union6.withItem2(expected);

        assertEquals(2, union.getIndex());
        Assertions.assertThrows(IllegalStateException.class, union::getItem0);
        Assertions.assertThrows(IllegalStateException.class, union::getItem1);
        assertEquals(expected, union.getItem2());
        Assertions.assertThrows(IllegalStateException.class, union::getItem3);
        Assertions.assertThrows(IllegalStateException.class, union::getItem4);
        Assertions.assertThrows(IllegalStateException.class, union::getItem5);
    }

    @Test
    void getItem3() {
        val expected = 3f;
        val union = Union6.withItem3(expected);

        assertEquals(3, union.getIndex());
        Assertions.assertThrows(IllegalStateException.class, union::getItem0);
        Assertions.assertThrows(IllegalStateException.class, union::getItem1);
        Assertions.assertThrows(IllegalStateException.class, union::getItem2);
        assertEquals(expected, union.getItem3());
        Assertions.assertThrows(IllegalStateException.class, union::getItem4);
        Assertions.assertThrows(IllegalStateException.class, union::getItem5);
    }

    @Test
    void getItem4() {
        val expected = true;
        val union = Union6.withItem4(expected);

        assertEquals(4, union.getIndex());
        Assertions.assertThrows(IllegalStateException.class, union::getItem0);
        Assertions.assertThrows(IllegalStateException.class, union::getItem1);
        Assertions.assertThrows(IllegalStateException.class, union::getItem2);
        Assertions.assertThrows(IllegalStateException.class, union::getItem3);
        assertEquals(expected, union.getItem4());
        Assertions.assertThrows(IllegalStateException.class, union::getItem5);
    }

    @Test
    void getItem5() {
        val expected = 5;
        val union = Union6.withItem5(expected);

        assertEquals(5, union.getIndex());
        Assertions.assertThrows(IllegalStateException.class, union::getItem0);
        Assertions.assertThrows(IllegalStateException.class, union::getItem1);
        Assertions.assertThrows(IllegalStateException.class, union::getItem2);
        Assertions.assertThrows(IllegalStateException.class, union::getItem3);
        Assertions.assertThrows(IllegalStateException.class, union::getItem4);
        assertEquals(expected, union.getItem5());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    void match() {
        Function[] creators = {
                Union6::withItem0,
                Union6::withItem1,
                Union6::withItem2,
                Union6::withItem3,
                Union6::withItem4,
                Union6::withItem5
        };

        IntStream.range(0, 6).forEach(expected -> {
            val union = (Union6) creators[expected].apply(expected);

            assertEquals(expected,
                    union.match(
                            Function.identity(),
                            Function.identity(),
                            Function.identity(),
                            Function.identity(),
                            Function.identity(),
                            Function.identity()));
        });
    }
}