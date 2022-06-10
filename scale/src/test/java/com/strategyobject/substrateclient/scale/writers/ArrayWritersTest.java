package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.common.utils.HexConverter;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.tests.TestSuite;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.ByteArrayOutputStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArrayWritersTest {
    @TestFactory
    Stream<DynamicTest> write() {
        val Array2DWriter = new ArrayWriter().inject(
                new ArrayWriter().inject(
                        new U16Writer()));

        return TestSuite.of(
                Test.write(new BooleanArrayWriter(),
                        new boolean[]{true, true, true, false, false, true},
                        "0x18010101000001"),
                Test.write(new ByteArrayWriter(),
                        new byte[]{4, 8, 15, 16, 23, 42},
                        "0x1804080f10172a"),
                Test.write(new ShortArrayWriter(),
                        new short[]{4, 8, 15, 16, 23, 42},
                        "0x18040008000f00100017002a00"),
                Test.write(new IntArrayWriter(),
                        new int[]{4, 8, 15, 16, 23, 42},
                        "0x1804000000080000000f00000010000000170000002a000000"),
                Test.write(new LongArrayWriter(),
                        new long[]{4, 8, 15, 16, 23, 42},
                        "0x18040000000000000008000000000000000f00000000000000100000000000000017000000000000002a00000000000000"),
                Test.write(new ArrayWriter().inject(new U16Writer()),
                                new Integer[]{4, 8, 15, 16, 23, 42},
                                "0x18040008000f00100017002a00")
                        .withWriterName(ArrayWriter.class.getSimpleName()),
                Test.write(Array2DWriter,
                                new Integer[][]{{4, 8, 15}, {16, 23, 42}},
                                "0x080c040008000f000c100017002a00")
                        .withWriterName("Array2DWriter")
        );
    }

    @TestFactory
    Stream<DynamicTest> writeEmpty() {
        return TestSuite.of(
                Test.writeEmpty(new BooleanArrayWriter(), new boolean[0]),
                Test.writeEmpty(new ByteArrayWriter(), new byte[0]),
                Test.writeEmpty(new ShortArrayWriter(), new short[0]),
                Test.writeEmpty(new IntArrayWriter(), new int[0]),
                Test.writeEmpty(new LongArrayWriter(), new long[0]),
                Test.writeEmpty(new ArrayWriter().inject(new U16Writer()), new Integer[0])
                        .withWriterName(ArrayWriter.class.getSimpleName())
        );
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class Test<T> extends TestSuite.TestCase {
        private final ScaleWriter<T> writer;
        private final T given;
        private final String expected;
        private String writerName;

        @Override
        public String getDisplayName() {
            return String.format("Write %s with %s", expected, writerName);
        }

        @Override
        public void execute() throws Throwable {
            val stream = new ByteArrayOutputStream();
            writer.write(given, stream);
            val actual = HexConverter.toHex(stream.toByteArray());
            assertEquals(expected, actual);
        }

        public Test<T> withWriterName(String writerName) {
            this.writerName = writerName;
            return this;
        }

        public static <T> Test<T> writeEmpty(ScaleWriter<T> writer, T given) {
            return new Test<>(writer, given, "0x00", writer.getClass().getSimpleName());
        }


        public static <T> Test<T> write(ScaleWriter<T> writer, T given, String expected) {
            return new Test<>(
                    writer,
                    given,
                    expected,
                    writer.getClass().getSimpleName());
        }
    }
}
