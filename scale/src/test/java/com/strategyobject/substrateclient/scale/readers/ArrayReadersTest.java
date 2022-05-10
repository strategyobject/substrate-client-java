package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.common.utils.HexConverter;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.tests.TestSuite;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class ArrayReadersTest {

    @TestFactory
    Stream<DynamicTest> read() {
        val Array2DReader = new ArrayReader().inject(
                new ArrayReader().inject(
                        new U16Reader()));

        return TestSuite.of(
                Test.read(new BooleanArrayReader(),
                        "0x18010101000001",
                        new boolean[]{true, true, true, false, false, true},
                        Assertions::assertArrayEquals),
                Test.read(new ByteArrayReader(),
                        "0x1804080f10172a",
                        new byte[]{4, 8, 15, 16, 23, 42},
                        Assertions::assertArrayEquals),
                Test.read(new ShortArrayReader(),
                        "0x18040008000f00100017002a00",
                        new short[]{4, 8, 15, 16, 23, 42},
                        Assertions::assertArrayEquals),
                Test.read(new IntArrayReader(),
                        "0x1804000000080000000f00000010000000170000002a000000",
                        new int[]{4, 8, 15, 16, 23, 42},
                        Assertions::assertArrayEquals),
                Test.read(new LongArrayReader(),
                        "0x18040000000000000008000000000000000f00000000000000100000000000000017000000000000002a00000000000000",
                        new long[]{4, 8, 15, 16, 23, 42},
                        Assertions::assertArrayEquals),
                Test.read(new ArrayReader().inject(new U16Reader()),
                                "0x18040008000f00100017002a00",
                                new Integer[]{4, 8, 15, 16, 23, 42},
                                Assertions::assertArrayEquals)
                        .withReaderName(ArrayReader.class.getSimpleName()),
                Test.read(Array2DReader,
                                "0x080c040008000f000c100017002a00",
                                new Integer[][]{{4, 8, 15}, {16, 23, 42}},
                                Assertions::assertArrayEquals)
                        .withReaderName("Array2DReader")
        );
    }

    @TestFactory
    Stream<DynamicTest> readEmpty() {
        return TestSuite.of(
                Test.readEmpty(new BooleanArrayReader(), new boolean[0], Assertions::assertArrayEquals),
                Test.readEmpty(new ByteArrayReader(), new byte[0], Assertions::assertArrayEquals),
                Test.readEmpty(new ShortArrayReader(), new short[0], Assertions::assertArrayEquals),
                Test.readEmpty(new IntArrayReader(), new int[0], Assertions::assertArrayEquals),
                Test.readEmpty(new LongArrayReader(), new long[0], Assertions::assertArrayEquals),
                Test.readEmpty(new ArrayReader().inject(new U16Reader()),
                                new Integer[0],
                                Assertions::assertArrayEquals)
                        .withReaderName(ArrayReader.class.getSimpleName())
        );
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class Test<T> extends TestSuite.TestCase {
        private final ScaleReader<T> reader;
        private final String given;
        private final T expected;
        private final BiConsumer<T, T> assertion;
        private String readerName;

        @Override
        public String getDisplayName() {
            return String.format("Read %s with %s", given, readerName);
        }

        @Override
        public void execute() throws IOException {
            val bytes = HexConverter.toBytes(given);
            val stream = new ByteArrayInputStream(bytes);

            val actual = reader.read(stream);
            assertion.accept(expected, actual);
        }

        public Test<T> withReaderName(String readerName) {
            this.readerName = readerName;
            return this;
        }

        public static <T> Test<T> readEmpty(ScaleReader<T> reader,
                                            T expected,
                                            BiConsumer<T, T> assertion) {
            return new Test<>(
                    reader,
                    "0x00",
                    expected,
                    assertion,
                    reader.getClass().getSimpleName());
        }

        public static <T> Test<T> read(ScaleReader<T> reader,
                                       String given,
                                       T expected,
                                       BiConsumer<T, T> assertion) {
            return new Test<>(
                    reader,
                    given,
                    expected,
                    assertion,
                    reader.getClass().getSimpleName());
        }
    }
}