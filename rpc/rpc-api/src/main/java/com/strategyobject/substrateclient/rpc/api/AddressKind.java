package com.strategyobject.substrateclient.rpc.api;

import lombok.Getter;

public enum AddressKind {
    ID((byte) 0);

    @Getter
    private final byte value;

    AddressKind(byte value) {
        this.value = value;
    }
}
