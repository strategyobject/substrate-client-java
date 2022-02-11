package com.strategyobject.substrateclient.transport;

import java.util.List;
import java.util.Map;

public abstract class RpcObject {

    RpcObject() {
    }

    public boolean isNull() {
        return false;
    }

    public boolean isBoolean() {
        return false;
    }

    public boolean isList() {
        return false;
    }

    public boolean isMap() {
        return false;
    }

    public boolean isNumber() {
        return false;
    }

    public boolean isString() {
        return false;
    }

    public Boolean asBoolean() {
        throw new IllegalStateException();
    }

    public List<RpcObject> asList() {
        throw new IllegalStateException();
    }

    public Map<String, RpcObject> asMap() {
        throw new IllegalStateException();
    }

    public Number asNumber() {
        throw new IllegalStateException();
    }

    public String asString() {
        throw new IllegalStateException();
    }

    public static RpcObject ofNull() {
        return new RpcNull();
    }

    public static RpcObject of(boolean value) {
        return new RpcBoolean(value);
    }

    public static RpcObject of(List<RpcObject> list) {
        return list == null ? new RpcNull() : new RpcList(list);
    }

    public static RpcObject of(Map<String, RpcObject> map) {
        return map == null ? new RpcNull() : new RpcMap(map);
    }

    public static RpcObject of(Number value) {
        return value == null ? new RpcNull() : new RpcNumber(value);
    }

    public static RpcObject of(String value) {
        return value == null ? new RpcNull() : new RpcString(value);
    }

}
