package com.strategyobject.substrateclient.transport;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public final class RpcList extends RpcObject {

    private final List<RpcObject> list;

    @Override
    public boolean isList() {
        return true;
    }

    @Override
    public List<RpcObject> asList() {
        return list;
    }

}
