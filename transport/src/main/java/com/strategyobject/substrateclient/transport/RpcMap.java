package com.strategyobject.substrateclient.transport;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public final class RpcMap implements RpcObject {

    private final Map<String, RpcObject> map;

    @Override
    public boolean isMap() {
        return true;
    }

    @Override
    public Map<String, RpcObject> asMap() {
        return map;
    }

}
