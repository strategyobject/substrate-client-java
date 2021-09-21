package com.strategyobject.substrateclient.rpc.types;

import lombok.Getter;

public enum SignatureKind {
    ED25519((byte) 0),
    SR25519((byte) 1),
    ECDSA((byte) 2);

    @Getter
    private final byte value;

    SignatureKind(byte value) {
        this.value = value;
    }
}
