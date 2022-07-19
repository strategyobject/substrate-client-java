package com.strategyobject.substrateclient.common.types;

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnumsTest {

    enum TestEnum {
        YES, NO, MAYBE
    }

    @Test
    void lookup() {
        val actual = Enums.lookup(TestEnum.values(), 0);

        assertEquals(TestEnum.YES, actual);
    }

    @Test
    void lookupOutOfBounds() {
        val values = TestEnum.values();
        val thrown = assertThrows(RuntimeException.class, () -> Enums.lookup(values, 10));

        assertTrue(thrown.getMessage().contains("TestEnum"));
    }
}