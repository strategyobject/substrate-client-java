package com.strategyobject.substrateclient.transport;

import java.util.List;
import java.util.Map;

public final class RpcNull extends RpcObject {
    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public boolean isBoolean() {
        return true;
    }

    @Override
    public boolean isList() {
        return true;
    }

    @Override
    public boolean isMap() {
        return true;
    }

    @Override
    public boolean isNumber() {
        return true;
    }

    @Override
    public boolean isString() {
        return true;
    }

    @Override
    public Boolean asBoolean() {
        return null;
    }

    @Override
    public List<RpcObject> asList() {
        return null;
    }

    @Override
    public Map<String, RpcObject> asMap() {
        return null;
    }

    @Override
    public Number asNumber() {
        return null;
    }

    @Override
    public String asString() {
        return null;
    }
}
