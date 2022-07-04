package com.strategyobject.substrateclient.api;

import com.google.inject.Module;
import com.strategyobject.substrateclient.pallet.PalletFactory;
import com.strategyobject.substrateclient.rpc.RpcSectionFactory;
import com.strategyobject.substrateclient.transport.ProviderInterface;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Provides the ability to query a node and interact with the Polkadot or Substrate chains.
 * It allows interacting with blockchain in various ways: using RPC's queries directly or
 * accessing Pallets and its APIs, such as storages, transactions, etc.
 */
@RequiredArgsConstructor
public class Api implements AutoCloseable {
    private final @NonNull RpcSectionFactory rpcSectionFactory;
    private final @NonNull PalletFactory palletFactory;
    private final Map<Class<?>, Object> resolvedCache = new ConcurrentHashMap<>();


    /**
     * Resolves the instance of a rpc by its definition.
     *
     * @param clazz the class of the rpc
     * @param <T>   the type of the rpc
     * @return appropriate instance of the rpc
     */
    public <T> T rpc(@NonNull Class<T> clazz) {
        return clazz.cast(resolvedCache.computeIfAbsent(clazz, rpcSectionFactory::create));
    }

    /**
     * Resolves the instance of a pallet by its definition.
     *
     * @param clazz the class of the pallet
     * @param <T>   the type of the pallet
     * @return appropriate instance of the pallet
     */
    public <T> T pallet(@NonNull Class<T> clazz) {
        return clazz.cast(resolvedCache.computeIfAbsent(clazz, palletFactory::create));
    }

    @Override
    public void close() throws Exception {
        if (rpcSectionFactory instanceof AutoCloseable) {
            ((AutoCloseable) rpcSectionFactory).close();
        }
    }

    public static ApiBuilder<DefaultModule> with(@NonNull Supplier<ProviderInterface> providerInterface) {
        return with(new DefaultModule(providerInterface.get()));
    }

    public static <M extends Module> ApiBuilder<M> with(@NonNull M module) {
        return new ApiBuilder<>(module);
    }
}