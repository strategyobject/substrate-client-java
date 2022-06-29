package com.strategyobject.substrateclient.rpc.api;

import com.strategyobject.substrateclient.scale.annotation.ScaleWriter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@ScaleWriter
public class SignaturePayload<A extends Address, S extends Signature, E extends Extra> {
    private final A address;
    private final S signature;
    private final E extra;
}
