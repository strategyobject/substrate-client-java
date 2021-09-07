package com.strategyobject.substrateclient.rpc.core;

public interface ParameterConverter {
    <T> Object convert(T param);
}
