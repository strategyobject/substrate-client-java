package com.strategyobject.substrateclient.rpc.api;

import lombok.Getter;

import java.util.Optional;

@Getter
public class Extrinsic<C extends Call, A extends Address, S extends Signature, E extends Extra> {
    private final Optional<SignaturePayload<A, S, E>> signature;
    private final C call;

    Extrinsic(Optional<SignaturePayload<A, S, E>> signature, C call) {
        this.signature = signature;
        this.call = call;
    }

    public static <C extends Call, A extends Address, S extends Signature, E extends Extra> Extrinsic<C, A, S, E> createSigned(
            SignaturePayload<A, S, E> signature,
            C call) {
        return new Extrinsic<>(Optional.of(signature), call);
    }

    public static <C extends Call> Extrinsic<C, ?, ?, ?> createUnsigned(C call) {
        return new Extrinsic<>(Optional.empty(), call);
    }
}