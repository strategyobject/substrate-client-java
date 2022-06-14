package com.strategyobject.substrateclient.transport;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class RpcString implements RpcObject {

    private final String value;

    @Override
    public boolean isString() {
        return true;
    }

    @Override
    public String asString() {
        return value;
    }

}
