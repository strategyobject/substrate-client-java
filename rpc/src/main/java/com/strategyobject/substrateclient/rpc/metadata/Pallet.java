package com.strategyobject.substrateclient.rpc.metadata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class Pallet {
    private final int index;
    private final @NonNull String name;
}
