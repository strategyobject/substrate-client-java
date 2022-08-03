package com.strategyobject.substrateclient.rpc.api.storage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName = "valueOf")
public class StorageData {
    private final byte[] data;
}
