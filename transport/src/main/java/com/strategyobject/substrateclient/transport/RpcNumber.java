package com.strategyobject.substrateclient.transport;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class RpcNumber implements RpcObject {

    private final Number value;

    @Override
    public boolean isNumber() {
        return true;
    }

    @Override
    public Number asNumber() {
        return value;
    }

}
