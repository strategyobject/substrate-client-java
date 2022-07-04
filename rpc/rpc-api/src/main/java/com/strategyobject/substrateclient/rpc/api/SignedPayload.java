package com.strategyobject.substrateclient.rpc.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SignedPayload<C extends Call, E extends Extra & SignedExtension> {
    private final C call;
    private final E extra;
}
