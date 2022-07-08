package com.strategyobject.substrateclient.rpc.metadata;

import com.google.common.base.Preconditions;
import lombok.NonNull;

import java.util.stream.IntStream;

public class PalletCollection {
    private final Pallet[] pallets;

    public PalletCollection(Pallet @NonNull ... pallets) {
        Preconditions.checkArgument(pallets.length > 0);
        Preconditions.checkArgument(pallets[0].getIndex() == 0);
        Preconditions.checkArgument(pallets.length == 1 ||
                IntStream.range(1, pallets.length)
                        .allMatch(i -> pallets[i].getIndex() == pallets[i - 1].getIndex() + 1));

        this.pallets = pallets;
    }

    public Pallet get(int index) {
        return pallets[index];
    }

    public int size() {
        return pallets.length;
    }
}
