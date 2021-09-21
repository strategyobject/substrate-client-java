package com.strategyobject.substrateclient.common.utils;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("ConstantConditions")
class StringUtilsTest {

    @Test
    void allIndexesOfAny() {
        val target = "Result<Integer, Boolean>";

        val actual = StringUtils.allIndexesOfAny(target, "<,>");

        assertEquals(Arrays.asList(6, 14, 23), actual);
    }

    @Test
    void allIndexesOfAnyFailsWhenTargetNull() {
        assertThrows(IllegalArgumentException.class, () -> StringUtils.allIndexesOfAny(null, "<,>"));
    }

    @Test
    void allIndexesOfAnyFailsWhenCharsNull() {
        val target = "Result<Integer, Boolean>";

        assertThrows(IllegalArgumentException.class, () -> StringUtils.allIndexesOfAny(target, null));
    }

    @Test
    void allIndexesOfAnyFailsWhenCharsEmpty() {
        val target = "Result<Integer, Boolean>";

        assertThrows(IllegalArgumentException.class, () -> StringUtils.allIndexesOfAny(target, ""));
    }
}