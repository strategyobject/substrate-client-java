package com.strategyobject.substrateclient.scale.codegen.reader;

import com.google.testing.compile.JavaFileObjects;
import com.strategyobject.substrateclient.tests.TestSuite;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

class ScaleReaderProcessorTest {
    @Test
    void failsWhenWrongTemplate() {
        val clazz = JavaFileObjects.forResource("WrongTemplate.java");

        val compilation = javac()
                .withProcessors(new ScaleReaderProcessor())
                .compile(clazz);

        assertThat(compilation).failed();
        assertThat(compilation)
                .hadErrorContaining("brackets");
    }

    @TestFactory
    Stream<DynamicTest> compiles() {
        return TestSuite.of(
                TestCase.compile("Annotated.java"),
                TestCase.compile("NonAnnotated.java"),
                TestCase.compile("ComplexGeneric.java"),
                TestCase.compile("Arrays.java"),
                TestCase.compile("Enum.java")
        );
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class TestCase implements TestSuite.TestCase {
        private final String filename;

        @Override
        public String getDisplayName() {
            return "compiles " + filename;
        }

        @Override
        public void execute() {
            val clazz = JavaFileObjects.forResource(filename);

            val compilation = javac()
                    .withProcessors(new ScaleReaderProcessor())
                    .compile(clazz);

            assertThat(compilation).succeeded();
        }

        public static TestCase compile(String filename) {
            return new TestCase(filename);
        }
    }
}
