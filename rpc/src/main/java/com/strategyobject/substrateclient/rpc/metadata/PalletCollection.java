package com.strategyobject.substrateclient.rpc.metadata;

import com.google.common.base.Preconditions;
import com.google.common.collect.FluentIterable;
import lombok.NonNull;

import java.util.Map;
import java.util.stream.IntStream;

public class PalletCollection {
    private final Map<Integer, Pallet> palletsIndex;

    public PalletCollection(Pallet @NonNull ... pallets) {
        Preconditions.checkArgument(pallets.length > 0);
        Preconditions.checkArgument(pallets[0].getIndex() == 0);
/*        Preconditions.checkArgument(pallets.length == 1 ||
                IntStream.range(1, pallets.length)
                        .allMatch(i -> pallets[i].getIndex() == pallets[i - 1].getIndex() + 1));*/

        palletsIndex = FluentIterable.from(pallets).uniqueIndex(Pallet::getIndex);
    }

    public Pallet get(int index) {
        return palletsIndex.get(index);
    }

    public int size() {
        return palletsIndex.size();
    }
}
