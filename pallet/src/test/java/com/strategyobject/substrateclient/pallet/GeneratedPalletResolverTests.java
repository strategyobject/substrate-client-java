package com.strategyobject.substrateclient.pallet;

import com.strategyobject.substrateclient.rpc.Rpc;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class GeneratedPalletResolverTests {
    @Test
    public void throwsWhenPalletIsNotAnnotated() {
        val rpc = mock(Rpc.class);

        val resolver = GeneratedPalletResolver.with(rpc);

        assertThrows(IllegalArgumentException.class,
                () -> resolver.resolve(TestPalletNotAnnotated.class));
    }

    @Test
    public void throwsWhenPalletImplementationDoesNotHaveAppropriateConstructor() {
        val rpc = mock(Rpc.class);

        val resolver = GeneratedPalletResolver.with(rpc);

        assertThrows(RuntimeException.class,
                () -> resolver.resolve(TestPalletWithoutConstructor.class));
    }

    @Test
    public void resolve() {
        val rpc = mock(Rpc.class);

        val resolver = GeneratedPalletResolver.with(rpc);
        val pallet = resolver.resolve(TestPallet.class);

        assertNotNull(pallet);
        assertEquals(TestPalletImpl.class, pallet.getClass());
    }
}
