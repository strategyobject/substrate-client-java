package com.strategyobject.substrateclient.rpc.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName = "valueOf")
public class StorageData {
    private final byte[] data;
}
