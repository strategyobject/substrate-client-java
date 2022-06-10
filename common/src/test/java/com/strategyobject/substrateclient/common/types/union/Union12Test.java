package com.strategyobject.substrateclient.common.types.union;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Union12Test {

    @Test
    void getItem0() {
        val expected = false;
        val union = Union12.withItem0(expected);

        assertEquals(0, union.getIndex());
        assertEquals(expected, union.getItem0());
        Assertions.assertThrows(IllegalStateException.class, union::getItem1);
        Assertions.assertThrows(IllegalStateException.class, union::getItem2);
        Assertions.assertThrows(IllegalStateException.class, union::getItem3);
        Assertions.assertThrows(IllegalStateException.class, union::getItem4);
        Assertions.assertThrows(IllegalStateException.class, union::getItem5);
        Assertions.assertThrows(IllegalStateException.class, union::getItem6);
        Assertions.assertThrows(IllegalStateException.class, union::getItem7);
        Assertions.assertThrows(IllegalStateException.class, union::getItem8);
        Assertions.assertThrows(IllegalStateException.class, union::getItem9);
        Assertions.assertThrows(IllegalStateException.class, union::getItem10);
        Assertions.assertThrows(IllegalStateException.class, union::getItem11);
    }

    @Test
    void getItem1() {
        val expected = 1;
        val union = Union12.withItem1(expected);

        assertEquals(1, union.getIndex());
        Assertions.assertThrows(IllegalStateException.class, union::getItem0);
        assertEquals(expected, union.getItem1());
        Assertions.assertThrows(IllegalStateException.class, union::getItem2);
        Assertions.assertThrows(IllegalStateException.class, union::getItem3);
        Assertions.assertThrows(IllegalStateException.class, union::getItem4);
        Assertions.assertThrows(IllegalStateException.class, union::getItem5);
        Assertions.assertThrows(IllegalStateException.class, union::getItem6);
        Assertions.assertThrows(IllegalStateException.class, union::getItem7);
        Assertions.assertThrows(IllegalStateException.class, union::getItem8);
        Assertions.assertThrows(IllegalStateException.class, union::getItem9);
        Assertions.assertThrows(IllegalStateException.class, union::getItem10);
        Assertions.assertThrows(IllegalStateException.class, union::getItem11);
    }

    @Test
    void getItem2() {
        val expected = "2";
        val union = Union12.withItem2(expected);

        assertEquals(2, union.getIndex());
        Assertions.assertThrows(IllegalStateException.class, union::getItem0);
        Assertions.assertThrows(IllegalStateException.class, union::getItem1);
        assertEquals(expected, union.getItem2());
        Assertions.assertThrows(IllegalStateException.class, union::getItem3);
        Assertions.assertThrows(IllegalStateException.class, union::getItem4);
        Assertions.assertThrows(IllegalStateException.class, union::getItem5);
        Assertions.assertThrows(IllegalStateException.class, union::getItem6);
        Assertions.assertThrows(IllegalStateException.class, union::getItem7);
        Assertions.assertThrows(IllegalStateException.class, union::getItem8);
        Assertions.assertThrows(IllegalStateException.class, union::getItem9);
        Assertions.assertThrows(IllegalStateException.class, union::getItem10);
        Assertions.assertThrows(IllegalStateException.class, union::getItem11);
    }

    @Test
    void getItem3() {
        val expected = 3f;
        val union = Union12.withItem3(expected);

        assertEquals(3, union.getIndex());
        Assertions.assertThrows(IllegalStateException.class, union::getItem0);
        Assertions.assertThrows(IllegalStateException.class, union::getItem1);
        Assertions.assertThrows(IllegalStateException.class, union::getItem2);
        assertEquals(expected, union.getItem3());
        Assertions.assertThrows(IllegalStateException.class, union::getItem4);
        Assertions.assertThrows(IllegalStateException.class, union::getItem5);
        Assertions.assertThrows(IllegalStateException.class, union::getItem6);
        Assertions.assertThrows(IllegalStateException.class, union::getItem7);
        Assertions.assertThrows(IllegalStateException.class, union::getItem8);
        Assertions.assertThrows(IllegalStateException.class, union::getItem9);
        Assertions.assertThrows(IllegalStateException.class, union::getItem10);
        Assertions.assertThrows(IllegalStateException.class, union::getItem11);
    }

    @Test
    void getItem4() {
        val expected = true;
        val union = Union12.withItem4(expected);

        assertEquals(4, union.getIndex());
        Assertions.assertThrows(IllegalStateException.class, union::getItem0);
        Assertions.assertThrows(IllegalStateException.class, union::getItem1);
        Assertions.assertThrows(IllegalStateException.class, union::getItem2);
        Assertions.assertThrows(IllegalStateException.class, union::getItem3);
        assertEquals(expected, union.getItem4());
        Assertions.assertThrows(IllegalStateException.class, union::getItem5);
        Assertions.assertThrows(IllegalStateException.class, union::getItem6);
        Assertions.assertThrows(IllegalStateException.class, union::getItem7);
        Assertions.assertThrows(IllegalStateException.class, union::getItem8);
        Assertions.assertThrows(IllegalStateException.class, union::getItem9);
        Assertions.assertThrows(IllegalStateException.class, union::getItem10);
        Assertions.assertThrows(IllegalStateException.class, union::getItem11);
    }

    @Test
    void getItem5() {
        val expected = 5;
        val union = Union12.withItem5(expected);

        assertEquals(5, union.getIndex());
        Assertions.assertThrows(IllegalStateException.class, union::getItem0);
        Assertions.assertThrows(IllegalStateException.class, union::getItem1);
        Assertions.assertThrows(IllegalStateException.class, union::getItem2);
        Assertions.assertThrows(IllegalStateException.class, union::getItem3);
        Assertions.assertThrows(IllegalStateException.class, union::getItem4);
        assertEquals(expected, union.getItem5());
        Assertions.assertThrows(IllegalStateException.class, union::getItem6);
        Assertions.assertThrows(IllegalStateException.class, union::getItem7);
        Assertions.assertThrows(IllegalStateException.class, union::getItem8);
        Assertions.assertThrows(IllegalStateException.class, union::getItem9);
        Assertions.assertThrows(IllegalStateException.class, union::getItem10);
        Assertions.assertThrows(IllegalStateException.class, union::getItem11);
    }

    @Test
    void getItem6() {
        val expected = "6";
        val union = Union12.withItem6(expected);

        assertEquals(6, union.getIndex());
        Assertions.assertThrows(IllegalStateException.class, union::getItem0);
        Assertions.assertThrows(IllegalStateException.class, union::getItem1);
        Assertions.assertThrows(IllegalStateException.class, union::getItem2);
        Assertions.assertThrows(IllegalStateException.class, union::getItem3);
        Assertions.assertThrows(IllegalStateException.class, union::getItem4);
        Assertions.assertThrows(IllegalStateException.class, union::getItem5);
        assertEquals(expected, union.getItem6());
        Assertions.assertThrows(IllegalStateException.class, union::getItem7);
        Assertions.assertThrows(IllegalStateException.class, union::getItem8);
        Assertions.assertThrows(IllegalStateException.class, union::getItem9);
        Assertions.assertThrows(IllegalStateException.class, union::getItem10);
        Assertions.assertThrows(IllegalStateException.class, union::getItem11);
    }

    @Test
    void getItem7() {
        val expected = 7f;
        val union = Union12.withItem7(expected);

        assertEquals(7, union.getIndex());
        Assertions.assertThrows(IllegalStateException.class, union::getItem0);
        Assertions.assertThrows(IllegalStateException.class, union::getItem1);
        Assertions.assertThrows(IllegalStateException.class, union::getItem2);
        Assertions.assertThrows(IllegalStateException.class, union::getItem3);
        Assertions.assertThrows(IllegalStateException.class, union::getItem4);
        Assertions.assertThrows(IllegalStateException.class, union::getItem5);
        Assertions.assertThrows(IllegalStateException.class, union::getItem6);
        assertEquals(expected, union.getItem7());
        Assertions.assertThrows(IllegalStateException.class, union::getItem8);
        Assertions.assertThrows(IllegalStateException.class, union::getItem9);
        Assertions.assertThrows(IllegalStateException.class, union::getItem10);
        Assertions.assertThrows(IllegalStateException.class, union::getItem11);
    }

    @Test
    void getItem8() {
        val expected = false;
        val union = Union12.withItem8(expected);

        assertEquals(8, union.getIndex());
        Assertions.assertThrows(IllegalStateException.class, union::getItem0);
        Assertions.assertThrows(IllegalStateException.class, union::getItem1);
        Assertions.assertThrows(IllegalStateException.class, union::getItem2);
        Assertions.assertThrows(IllegalStateException.class, union::getItem3);
        Assertions.assertThrows(IllegalStateException.class, union::getItem4);
        Assertions.assertThrows(IllegalStateException.class, union::getItem5);
        Assertions.assertThrows(IllegalStateException.class, union::getItem6);
        Assertions.assertThrows(IllegalStateException.class, union::getItem7);
        assertEquals(expected, union.getItem8());
        Assertions.assertThrows(IllegalStateException.class, union::getItem9);
        Assertions.assertThrows(IllegalStateException.class, union::getItem10);
        Assertions.assertThrows(IllegalStateException.class, union::getItem11);
    }

    @Test
    void getItem9() {
        val expected = 9;
        val union = Union12.withItem9(expected);

        assertEquals(9, union.getIndex());
        Assertions.assertThrows(IllegalStateException.class, union::getItem0);
        Assertions.assertThrows(IllegalStateException.class, union::getItem1);
        Assertions.assertThrows(IllegalStateException.class, union::getItem2);
        Assertions.assertThrows(IllegalStateException.class, union::getItem3);
        Assertions.assertThrows(IllegalStateException.class, union::getItem4);
        Assertions.assertThrows(IllegalStateException.class, union::getItem5);
        Assertions.assertThrows(IllegalStateException.class, union::getItem6);
        Assertions.assertThrows(IllegalStateException.class, union::getItem7);
        Assertions.assertThrows(IllegalStateException.class, union::getItem8);
        assertEquals(expected, union.getItem9());
        Assertions.assertThrows(IllegalStateException.class, union::getItem10);
        Assertions.assertThrows(IllegalStateException.class, union::getItem11);
    }

    @Test
    void getItem10() {
        val expected = "10";
        val union = Union12.withItem10(expected);

        assertEquals(10, union.getIndex());
        Assertions.assertThrows(IllegalStateException.class, union::getItem0);
        Assertions.assertThrows(IllegalStateException.class, union::getItem1);
        Assertions.assertThrows(IllegalStateException.class, union::getItem2);
        Assertions.assertThrows(IllegalStateException.class, union::getItem3);
        Assertions.assertThrows(IllegalStateException.class, union::getItem4);
        Assertions.assertThrows(IllegalStateException.class, union::getItem5);
        Assertions.assertThrows(IllegalStateException.class, union::getItem6);
        Assertions.assertThrows(IllegalStateException.class, union::getItem7);
        Assertions.assertThrows(IllegalStateException.class, union::getItem8);
        Assertions.assertThrows(IllegalStateException.class, union::getItem9);
        assertEquals(expected, union.getItem10());
        Assertions.assertThrows(IllegalStateException.class, union::getItem11);
    }

    @Test
    void getItem11() {
        val expected = 11f;
        val union = Union12.withItem11(expected);

        assertEquals(11, union.getIndex());
        Assertions.assertThrows(IllegalStateException.class, union::getItem0);
        Assertions.assertThrows(IllegalStateException.class, union::getItem1);
        Assertions.assertThrows(IllegalStateException.class, union::getItem2);
        Assertions.assertThrows(IllegalStateException.class, union::getItem3);
        Assertions.assertThrows(IllegalStateException.class, union::getItem4);
        Assertions.assertThrows(IllegalStateException.class, union::getItem5);
        Assertions.assertThrows(IllegalStateException.class, union::getItem6);
        Assertions.assertThrows(IllegalStateException.class, union::getItem7);
        Assertions.assertThrows(IllegalStateException.class, union::getItem8);
        Assertions.assertThrows(IllegalStateException.class, union::getItem9);
        Assertions.assertThrows(IllegalStateException.class, union::getItem10);
        assertEquals(expected, union.getItem11());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    void match() {
        Function[] creators = {
                Union12::withItem0,
                Union12::withItem1,
                Union12::withItem2,
                Union12::withItem3,
                Union12::withItem4,
                Union12::withItem5,
                Union12::withItem6,
                Union12::withItem7,
                Union12::withItem8,
                Union12::withItem9,
                Union12::withItem10,
                Union12::withItem11
        };

        IntStream.range(0, 12).forEach(expected -> {
            val union = (Union12) creators[expected].apply(expected);

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
                            Function.identity(),
                            Function.identity(),
                            Function.identity(),
                            Function.identity()));
        });
    }
}