package com.strategyobject.substrateclient.api;

import com.strategyobject.substrateclient.pallet.GeneratedPalletResolver;
import com.strategyobject.substrateclient.pallet.PalletResolver;
import com.strategyobject.substrateclient.rpc.RpcGeneratedSectionFactory;
import com.strategyobject.substrateclient.rpc.api.section.State;
import com.strategyobject.substrateclient.transport.ProviderInterface;
import lombok.NonNull;
import lombok.val;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Provides the ability to query a node and interact with the Polkadot or Substrate chains.
 * It allows interacting with blockchain in various ways: using RPC's queries directly or
 * accessing Pallets and its APIs, such as storages, transactions, etc.
 */
public class Api implements AutoCloseable {
    private final @NonNull ProviderInterface providerInterface;
    private final @NonNull PalletResolver palletResolver;
    private final Map<Class<?>, Object> resolvedCache = new ConcurrentHashMap<>();

    private Api(@NonNull ProviderInterface providerInterface) {
        this.providerInterface = providerInterface;

        val state = RpcGeneratedSectionFactory.create(State.class, providerInterface);
        this.palletResolver = GeneratedPalletResolver.with(state);
    }

    /**
     * Resolves the instance of a rpc by its definition.
     *
     * @param clazz the class of the rpc
     * @param <T>   the type of the rpc
     * @return appropriate instance of the rpc
     */
    public <T> T rpc(Class<T> clazz) {
        return clazz.cast(resolvedCache
                .computeIfAbsent(clazz, x -> RpcGeneratedSectionFactory.create(x, providerInterface)));
    }

    /**
     * Resolves the instance of a pallet by its definition.
     *
     * @param clazz the class of the pallet
     * @param <T>   the type of the pallet
     * @return appropriate instance of the pallet
     */
    public <T> T pallet(@NonNull Class<T> clazz) {
        return clazz.cast(resolvedCache
                .computeIfAbsent(clazz, palletResolver::resolve));
    }

    public static Api with(ProviderInterface providerInterface) {
        return new Api(providerInterface);
    }

    @Override
    public void close() throws Exception {
        if (providerInterface instanceof AutoCloseable) {
            ((AutoCloseable) providerInterface).close();
        }
    }
}