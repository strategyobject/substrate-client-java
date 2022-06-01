package com.strategyobject.substrateclient.rpc.api;

import lombok.Getter;

public enum EraKind {
    IMMORTAL((byte) 0),
    MORTAL((byte) 1);

    @Getter
    private final byte value;

    EraKind(byte value) {
        this.value = value;
    }
}
