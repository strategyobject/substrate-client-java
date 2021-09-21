package com.strategyobject.substrateclient.rpc.types;

import com.strategyobject.substrateclient.scale.ScaleEncoded;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SignaturePayload<A extends Address, S extends Signature, E extends Extra> implements ScaleEncoded<SignaturePayload<A, S, E>> {
    private final A address;
    private final S signature;
    private final E extra;
}
