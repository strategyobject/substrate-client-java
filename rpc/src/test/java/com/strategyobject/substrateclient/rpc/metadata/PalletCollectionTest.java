package com.strategyobject.substrateclient.rpc.metadata;

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PalletCollectionTest {
    @Test
    void get() {
        val pallet1 = mock(Pallet.class);
        when(pallet1.getIndex()).thenReturn(0);
        val pallet2 = mock(Pallet.class);
        when(pallet2.getIndex()).thenReturn(1);
        val pallet3 = mock(Pallet.class);
        when(pallet3.getIndex()).thenReturn(2);

        PalletCollection palletCollection = new PalletCollection(
                pallet1,
                pallet2,
                pallet3
        );

        assertEquals(3, palletCollection.size());
        assertEquals(pallet1, palletCollection.get(0));
        assertEquals(pallet2, palletCollection.get(1));
        assertEquals(pallet3, palletCollection.get(2));
    }

    @Test
    void failsWhenIndexIsNotIncremental() {
        val pallet1 = mock(Pallet.class);
        when(pallet1.getIndex()).thenReturn(0);
        val pallet2 = mock(Pallet.class);
        when(pallet2.getIndex()).thenReturn(1);
        val pallet3 = mock(Pallet.class);
        when(pallet3.getIndex()).thenReturn(1);

        assertThrows(IllegalArgumentException.class, () -> new PalletCollection(pallet1, pallet2, pallet3));
    }
}