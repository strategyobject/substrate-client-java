package com.strategyobject.substrateclient.transport;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class RpcBoolean extends RpcObject {

    private final boolean value;

    @Override
    public boolean isBoolean() {
        return true;
    }

    @Override
    public Boolean asBoolean() {
        return value;
    }

}
