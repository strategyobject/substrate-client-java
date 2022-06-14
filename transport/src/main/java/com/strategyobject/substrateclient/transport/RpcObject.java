package com.strategyobject.substrateclient.transport;

import java.util.List;
import java.util.Map;

public interface RpcObject {

    default boolean isNull() {
        return false;
    }

    default boolean isBoolean() {
        return false;
    }

    default boolean isList() {
        return false;
    }

    default boolean isMap() {
        return false;
    }

    default boolean isNumber() {
        return false;
    }

    default boolean isString() {
        return false;
    }

    default Boolean asBoolean() {
        throw new IllegalStateException();
    }

    default List<RpcObject> asList() {
        throw new IllegalStateException();
    }

    default Map<String, RpcObject> asMap() {
        throw new IllegalStateException();
    }

    default Number asNumber() {
        throw new IllegalStateException();
    }

    default String asString() {
        throw new IllegalStateException();
    }

    static RpcObject ofNull() {
        return new RpcNull();
    }

    static RpcObject of(boolean value) {
        return new RpcBoolean(value);
    }

    static RpcObject of(List<RpcObject> list) {
        return list == null ? new RpcNull() : new RpcList(list);
    }

    static RpcObject of(Map<String, RpcObject> map) {
        return map == null ? new RpcNull() : new RpcMap(map);
    }

    static RpcObject of(long value) {
        return new RpcNumber(value);
    }

    static RpcObject of(Number value) {
        return value == null ? new RpcNull() : new RpcNumber(value);
    }

    static RpcObject of(String value) {
        return value == null ? new RpcNull() : new RpcString(value);
    }

}
