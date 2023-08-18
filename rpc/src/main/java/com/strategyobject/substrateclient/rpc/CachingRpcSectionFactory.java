package com.strategyobject.substrateclient.rpc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class CachingRpcSectionFactory implements RpcSectionFactory {
    private final RpcSectionFactory underlying;
    private final Map<Class<?>, Object> cache = new ConcurrentHashMap<>();

    @Override
    public <T> T create(@NonNull Class<T> clazz) {
        return clazz.cast(cache.computeIfAbsent(clazz, underlying::create));
    }
}
