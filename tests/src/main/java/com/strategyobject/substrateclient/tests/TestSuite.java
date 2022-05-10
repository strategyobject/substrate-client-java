package com.strategyobject.substrateclient.tests;

import org.junit.jupiter.api.DynamicTest;

import java.util.Arrays;
import java.util.stream.Stream;


public class TestSuite {
    public static Stream<DynamicTest> of(TestCase... testCases) {
        return Arrays.stream(testCases).map(TestCase::generate);
    }

    private TestSuite() {
    }

    public abstract static class TestCase {
        public abstract String getDisplayName();

        public abstract void execute() throws Throwable;

        public DynamicTest generate() {
            return DynamicTest.dynamicTest(getDisplayName(), this::execute);
        }
    }
}


