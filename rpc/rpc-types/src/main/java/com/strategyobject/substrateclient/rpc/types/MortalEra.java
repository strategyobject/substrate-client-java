package com.strategyobject.substrateclient.rpc.types;

import lombok.Getter;

@Getter
public class MortalEra implements Era {
    private final int encoded;

    MortalEra(int encoded) {
        this.encoded = encoded;
    }
}
