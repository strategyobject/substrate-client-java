package com.strategyobject.substrateclient.types.union;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Union9Test {

    @Test
    void getItem0() {
        val expected = false;
        val union = Union9.withItem0(expected);

        Assertions.assertEquals(0, union.getIndex());
        Assertions.assertEquals(expected, union.getItem0());
        Assertions.assertThrows(IllegalStateException.class, union::getItem1);
        Assertions.assertThrows(IllegalStateException.class, union::getItem2);
        Assertions.assertThrows(IllegalStateException.class, union::getItem3);
        Assertions.assertThrows(IllegalStateException.class, union::getItem4);
        Assertions.assertThrows(IllegalStateException.class, union::getItem5);
        Assertions.assertThrows(IllegalStateException.class, union::getItem6);
        Assertions.assertThrows(IllegalStateException.class, union::getItem7);
        Assertions.assertThrows(IllegalStateException.class, union::getItem8);
    }

    @Test
    void getItem1() {
        val expected = 1;
        val union = Union9.withItem1(expected);

        Assertions.assertEquals(1, union.getIndex());
        Assertions.assertThrows(IllegalStateException.class, union::getItem0);
        Assertions.assertEquals(expected, union.getItem1());
        Assertions.assertThrows(IllegalStateException.class, union::getItem2);
        Assertions.assertThrows(IllegalStateException.class, union::getItem3);
        Assertions.assertThrows(IllegalStateException.class, union::getItem4);
        Assertions.assertThrows(IllegalStateException.class, union::getItem5);
        Assertions.assertThrows(IllegalStateException.class, union::getItem6);
        Assertions.assertThrows(IllegalStateException.class, union::getItem7);
        Assertions.assertThrows(IllegalStateException.class, union::getItem8);
    }

    @Test
    void getItem2() {
        val expected = "2";
        val union = Union9.withItem2(expected);

        Assertions.assertEquals(2, union.getIndex());
        Assertions.assertThrows(IllegalStateException.class, union::getItem0);
        Assertions.assertThrows(IllegalStateException.class, union::getItem1);
        Assertions.assertEquals(expected, union.getItem2());
        Assertions.assertThrows(IllegalStateException.class, union::getItem3);
        Assertions.assertThrows(IllegalStateException.class, union::getItem4);
        Assertions.assertThrows(IllegalStateException.class, union::getItem5);
        Assertions.assertThrows(IllegalStateException.class, union::getItem6);
        Assertions.assertThrows(IllegalStateException.class, union::getItem7);
        Assertions.assertThrows(IllegalStateException.class, union::getItem8);
    }

    @Test
    void getItem3() {
        val expected = 3f;
        val union = Union9.withItem3(expected);

        Assertions.assertEquals(3, union.getIndex());
        Assertions.assertThrows(IllegalStateException.class, union::getItem0);
        Assertions.assertThrows(IllegalStateException.class, union::getItem1);
        Assertions.assertThrows(IllegalStateException.class, union::getItem2);
        Assertions.assertEquals(expected, union.getItem3());
        Assertions.assertThrows(IllegalStateException.class, union::getItem4);
        Assertions.assertThrows(IllegalStateException.class, union::getItem5);
        Assertions.assertThrows(IllegalStateException.class, union::getItem6);
        Assertions.assertThrows(IllegalStateException.class, union::getItem7);
        Assertions.assertThrows(IllegalStateException.class, union::getItem8);
    }

    @Test
    void getItem4() {
        val expected = true;
        val union = Union9.withItem4(expected);

        Assertions.assertEquals(4, union.getIndex());
        Assertions.assertThrows(IllegalStateException.class, union::getItem0);
        Assertions.assertThrows(IllegalStateException.class, union::getItem1);
        Assertions.assertThrows(IllegalStateException.class, union::getItem2);
        Assertions.assertThrows(IllegalStateException.class, union::getItem3);
        Assertions.assertEquals(expected, union.getItem4());
        Assertions.assertThrows(IllegalStateException.class, union::getItem5);
        Assertions.assertThrows(IllegalStateException.class, union::getItem6);
        Assertions.assertThrows(IllegalStateException.class, union::getItem7);
        Assertions.assertThrows(IllegalStateException.class, union::getItem8);
    }

    @Test
    void getItem5() {
        val expected = 5;
        val union = Union9.withItem5(expected);

        Assertions.assertEquals(5, union.getIndex());
        Assertions.assertThrows(IllegalStateException.class, union::getItem0);
        Assertions.assertThrows(IllegalStateException.class, union::getItem1);
        Assertions.assertThrows(IllegalStateException.class, union::getItem2);
        Assertions.assertThrows(IllegalStateException.class, union::getItem3);
        Assertions.assertThrows(IllegalStateException.class, union::getItem4);
        Assertions.assertEquals(expected, union.getItem5());
        Assertions.assertThrows(IllegalStateException.class, union::getItem6);
        Assertions.assertThrows(IllegalStateException.class, union::getItem7);
        Assertions.assertThrows(IllegalStateException.class, union::getItem8);
    }

    @Test
    void getItem6() {
        val expected = "6";
        val union = Union9.withItem6(expected);

        Assertions.assertEquals(6, union.getIndex());
        Assertions.assertThrows(IllegalStateException.class, union::getItem0);
        Assertions.assertThrows(IllegalStateException.class, union::getItem1);
        Assertions.assertThrows(IllegalStateException.class, union::getItem2);
        Assertions.assertThrows(IllegalStateException.class, union::getItem3);
        Assertions.assertThrows(IllegalStateException.class, union::getItem4);
        Assertions.assertThrows(IllegalStateException.class, union::getItem5);
        Assertions.assertEquals(expected, union.getItem6());
        Assertions.assertThrows(IllegalStateException.class, union::getItem7);
        Assertions.assertThrows(IllegalStateException.class, union::getItem8);
    }

    @Test
    void getItem7() {
        val expected = false;
        val union = Union9.withItem7(expected);

        Assertions.assertEquals(7, union.getIndex());
        Assertions.assertThrows(IllegalStateException.class, union::getItem0);
        Assertions.assertThrows(IllegalStateException.class, union::getItem1);
        Assertions.assertThrows(IllegalStateException.class, union::getItem2);
        Assertions.assertThrows(IllegalStateException.class, union::getItem3);
        Assertions.assertThrows(IllegalStateException.class, union::getItem4);
        Assertions.assertThrows(IllegalStateException.class, union::getItem5);
        Assertions.assertThrows(IllegalStateException.class, union::getItem6);
        Assertions.assertEquals(expected, union.getItem7());
        Assertions.assertThrows(IllegalStateException.class, union::getItem8);
    }

    @Test
    void getItem8() {
        val expected = 8;
        val union = Union9.withItem8(expected);

        Assertions.assertEquals(8, union.getIndex());
        Assertions.assertThrows(IllegalStateException.class, union::getItem0);
        Assertions.assertThrows(IllegalStateException.class, union::getItem1);
        Assertions.assertThrows(IllegalStateException.class, union::getItem2);
        Assertions.assertThrows(IllegalStateException.class, union::getItem3);
        Assertions.assertThrows(IllegalStateException.class, union::getItem4);
        Assertions.assertThrows(IllegalStateException.class, union::getItem5);
        Assertions.assertThrows(IllegalStateException.class, union::getItem6);
        Assertions.assertThrows(IllegalStateException.class, union::getItem7);
        Assertions.assertEquals(expected, union.getItem8());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    void match() {
        Function[] creators = {
                Union9::withItem0,
                Union9::withItem1,
                Union9::withItem2,
                Union9::withItem3,
                Union9::withItem4,
                Union9::withItem5,
                Union9::withItem6,
                Union9::withItem7,
                Union9::withItem8
        };

        IntStream.range(0, 9).forEach(expected -> {
            val union = (Union9) creators[expected].apply(expected);

            assertEquals(expected,
                    union.match(
                            Function.identity(),
                            Function.identity(),
                            Function.identity(),
                            Function.identity(),
                            Function.identity(),
                            Function.identity(),
                            Function.identity(),
                            Function.identity(),
                            Function.identity()));
        });
    }
}