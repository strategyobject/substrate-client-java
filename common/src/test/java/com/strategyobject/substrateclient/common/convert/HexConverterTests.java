package com.strategyobject.substrateclient.common.convert;

import com.strategyobject.substrateclient.tests.TestSuite;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class HexConverterTests {
    @TestFactory
    Stream<DynamicTest> toHex() {
        return TestSuite.of(
                Test.toHex(new byte[]{0}, "0x00"),
                Test.toHex(new byte[]{(byte) 255}, "0xff"),
                Test.toHex(new byte[]{(byte) 255, 0}, "0xff00"),
                Test.toHex(new byte[]{(byte) 0, (byte) 255}, "0x00ff"),
                Test.toHex(new byte[]{127, (byte) 128}, "0x7f80"),
                Test.toHex(new byte[]{(byte) 128, (byte) 127}, "0x807f"),
                Test.toHex(new byte[0], "0x")
        );
    }

    @TestFactory
    Stream<DynamicTest> toHexThrows() {
        return TestSuite.of(
                Test.toHex(null, IllegalArgumentException.class)
        );
    }

    @TestFactory
    Stream<DynamicTest> toBytes() {
        return TestSuite.of(
                Test.toBytes("0x00", new byte[]{0}),
                Test.toBytes("0X00", new byte[]{0}),
                Test.toBytes("00", new byte[]{0}),
                Test.toBytes("007f", new byte[]{0, 127}),
                Test.toBytes("ff", new byte[]{(byte) 255}),
                Test.toBytes("FF", new byte[]{(byte) 255}),
                Test.toBytes("0x807f", new byte[]{(byte) 128, (byte) 127}),
                Test.toBytes("0x000102030a0b0c0d0e0f", new byte[]{0, 1, 2, 3, 0x0a, 11, 12, 13, 14, 0x0f}));
    }

    @TestFactory
    Stream<DynamicTest> toBytesThrows() {
        return TestSuite.of(
                Test.toBytes(null, IllegalArgumentException.class),
                Test.toBytes("0x0g", IllegalArgumentException.class),
                Test.toBytes("0x0G", IllegalArgumentException.class),
                Test.toBytes("0x!a", IllegalArgumentException.class),
                Test.toBytes("0x!%", IllegalArgumentException.class));
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class Test extends TestSuite.TestCase {
        private final String displayName;
        private final Executable executable;

        @Override
        public String getDisplayName() {
            return displayName;
        }

        @Override
        public void execute() throws Throwable {
            executable.execute();
        }

        public static Test toHex(byte[] given, String expected) {
            return new Test(
                    String.format("Convert %s to %s", Arrays.toString(given), expected),
                    () -> assertEquals(expected, HexConverter.toHex(given)));
        }

        public static <T extends Throwable> Test toHex(byte[] given, Class<T> expected) {
            return new Test(
                    String.format("Convert of %s throws %s", Arrays.toString(given), expected),
                    () -> assertThrows(expected, () -> HexConverter.toHex(given)));
        }

        public static Test toBytes(String given, byte[] expected) {
            return new Test(
                    String.format("Convert %s to %s", given, Arrays.toString(expected)),
                    () -> assertArrayEquals(expected, HexConverter.toBytes(given)));
        }

        public static <T extends Throwable> Test toBytes(String given, Class<T> expected) {
            return new Test(
                    String.format("Convert %s throws %s", given, expected),
                    () -> assertThrows(expected, () -> HexConverter.toBytes(given)));
        }
    }
}
