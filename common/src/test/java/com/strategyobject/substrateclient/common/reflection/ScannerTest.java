package com.strategyobject.substrateclient.common.reflection;

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScannerTest {

    @Test
    void getSubTypesOf() {
        val subtypes = Scanner.getSubTypesOf(TestInterface.class);

        assertNotNull(subtypes);
        assertEquals(1, subtypes.size());
        assertTrue(subtypes.contains(TestSubtype.class));
    }
}