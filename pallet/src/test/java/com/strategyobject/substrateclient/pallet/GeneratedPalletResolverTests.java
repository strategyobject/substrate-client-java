package com.strategyobject.substrateclient.pallet;

import com.strategyobject.substrateclient.rpc.api.section.State;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GeneratedPalletResolverTests {
    @Test
    void throwsWhenPalletIsNotAnnotated() {
        val state = mock(State.class);

        val resolver = GeneratedPalletResolver.with(state);

        assertThrows(IllegalArgumentException.class,
                () -> resolver.resolve(TestPalletNotAnnotated.class));
    }

    @Test
    void throwsWhenPalletImplementationDoesNotHaveAppropriateConstructor() {
        val state = mock(State.class);

        val resolver = GeneratedPalletResolver.with(state);

        assertThrows(RuntimeException.class,
                () -> resolver.resolve(TestPalletWithoutConstructor.class));
    }

    @Test
    void resolve() {
        val state = mock(State.class);

        val resolver = GeneratedPalletResolver.with(state);
        val pallet = resolver.resolve(TestPallet.class);

        assertNotNull(pallet);
        assertEquals(TestPalletImpl.class, pallet.getClass());
    }
}
