package com.strategyobject.substrateclient.pallet;

import com.strategyobject.substrateclient.rpc.api.section.State;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GeneratedPalletFactoryTests {
    private final GeneratedPalletFactory factory = new GeneratedPalletFactory(
            TestsHelper.SCALE_READER_REGISTRY,
            TestsHelper.SCALE_WRITER_REGISTRY,
            mock(State.class));

    @Test
    void throwsWhenPalletIsNotAnnotated() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(TestPalletNotAnnotated.class));
    }

    @Test
    void throwsWhenPalletImplementationDoesNotHaveAppropriateConstructor() {
        assertThrows(RuntimeException.class,
                () -> factory.create(TestPalletWithoutConstructor.class));
    }

    @Test
    void resolve() {
        val pallet = factory.create(TestPallet.class);

        assertNotNull(pallet);
        assertEquals(TestPalletImpl.class, pallet.getClass());
    }
}
