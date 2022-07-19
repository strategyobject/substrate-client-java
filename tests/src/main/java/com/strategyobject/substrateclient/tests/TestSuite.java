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

    public interface TestCase {
        String getDisplayName();

        void execute() throws Throwable;

        default DynamicTest generate() {
            return DynamicTest.dynamicTest(getDisplayName(), this::execute);
        }
    }
}


