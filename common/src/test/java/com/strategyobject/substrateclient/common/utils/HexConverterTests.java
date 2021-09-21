package com.strategyobject.substrateclient.common.utils;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class HexConverterTests {
    @Test
    void toHex() {
        val testCases = new ArrayList<TestCase<byte[], String>>();
        testCases.add(new TestCase<>(new byte[]{0}, "0x00"));
        testCases.add(new TestCase<>(new byte[]{(byte) 255}, "0xff"));
        testCases.add(new TestCase<>(new byte[]{(byte) 255, 0}, "0xff00"));
        testCases.add(new TestCase<>(new byte[]{(byte) 0, (byte) 255}, "0x00ff"));
        testCases.add(new TestCase<>(new byte[]{127, (byte) 128}, "0x7f80"));
        testCases.add(new TestCase<>(new byte[]{(byte) 128, (byte) 127}, "0x807f"));

        for (val test : testCases) {
            assertEquals(test.expected, HexConverter.toHex(test.given));
        }
    }

    @Test
    void toHexThrows() {
        val testCases = new ArrayList<TestCase<byte[], Class<? extends Throwable>>>();
        testCases.add(new TestCase<>(null, IllegalArgumentException.class));
        testCases.add(new TestCase<>(new byte[]{}, IllegalArgumentException.class));

        for (val test : testCases) {
            assertThrows(test.expected, () -> HexConverter.toHex(test.given));
        }
    }

    @Test
    void toBytes() {
        val testCases = new ArrayList<TestCase<String, byte[]>>();
        testCases.add(new TestCase<>("0x00", new byte[]{0}));
        testCases.add(new TestCase<>("0X00", new byte[]{0}));
        testCases.add(new TestCase<>("00", new byte[]{0}));
        testCases.add(new TestCase<>("007f", new byte[]{0, 127}));
        testCases.add(new TestCase<>("ff", new byte[]{(byte) 255}));
        testCases.add(new TestCase<>("FF", new byte[]{(byte) 255}));
        testCases.add(new TestCase<>("0x807f", new byte[]{(byte) 128, (byte) 127}));
        testCases.add(new TestCase<>(
                "0x000102030a0b0c0d0e0f",
                new byte[]{
                        0,
                        1,
                        2,
                        3,
                        0x0a,
                        11,
                        12,
                        13,
                        14,
                        0x0f}));

        for (val test : testCases) {
            assertArrayEquals(test.expected, HexConverter.toBytes(test.given));
        }
    }

    @Test
    void toBytesThrows() {
        val testCases = new ArrayList<TestCase<String, Class<? extends Throwable>>>();
        testCases.add(new TestCase<>(null, IllegalArgumentException.class));
        testCases.add(new TestCase<>("0x0g", IllegalArgumentException.class));
        testCases.add(new TestCase<>("0x0G", IllegalArgumentException.class));
        testCases.add(new TestCase<>("0x!a", IllegalArgumentException.class));
        testCases.add(new TestCase<>("0x!%", IllegalArgumentException.class));

        for (val test : testCases) {
            assertThrows(test.expected, () -> HexConverter.toBytes(test.given));
        }
    }

    @RequiredArgsConstructor
    static class TestCase<T1, T2> {
        final T1 given;
        final T2 expected;
    }
}
