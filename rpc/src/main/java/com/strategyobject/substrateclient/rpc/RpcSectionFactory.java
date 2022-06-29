package com.strategyobject.substrateclient.rpc;

import lombok.NonNull;

public interface RpcSectionFactory {
    <T> T create(@NonNull Class<T> interfaceClass);
}
