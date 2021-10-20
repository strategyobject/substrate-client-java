package com.strategyobject.substrateclient.rpc.types;

import com.strategyobject.substrateclient.scale.ScaleSelfWritable;
import com.strategyobject.substrateclient.scale.annotations.ScaleWriter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@ScaleWriter
public class SignaturePayload<A extends Address, S extends Signature, E extends Extra> implements ScaleSelfWritable<SignaturePayload<A, S, E>> {
    private final A address;
    private final S signature;
    private final E extra;
}
