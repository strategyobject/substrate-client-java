package com.strategyobject.substrateclient.api;

import com.strategyobject.substrateclient.pallet.PalletResolver;
import com.strategyobject.substrateclient.rpc.Rpc;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor(staticName = "with")
public class DefaultApi implements Api, AutoCloseable {
    private final @NonNull Rpc rpc;
    private final @NonNull PalletResolver palletResolver;
    private final Map<Class<?>, Object> palletCache = new ConcurrentHashMap<>();

    @Override
    public Rpc rpc() {
        return rpc;
    }

    @Override
    public <T> T pallet(@NonNull Class<T> clazz) {
        return clazz.cast(palletCache
                .computeIfAbsent(clazz, palletResolver::resolve));
    }

    @Override
    public void close() throws Exception {
        if (rpc instanceof AutoCloseable) {
            ((AutoCloseable) rpc).close();
        }
    }
}