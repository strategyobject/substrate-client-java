package com.strategyobject.substrateclient.api;

import com.strategyobject.substrateclient.pallet.GeneratedPalletResolver;
import com.strategyobject.substrateclient.rpc.Rpc;
import com.strategyobject.substrateclient.rpc.RpcImpl;
import com.strategyobject.substrateclient.transport.ProviderInterface;
import lombok.val;

/**
 * Provides the ability to query a node and interact with the Polkadot or Substrate chains.
 * It allows interacting with blockchain in various ways: using RPC's queries directly or
 * accessing Pallets and its APIs, such as storages, transactions, etc.
 */
public interface Api {
    static DefaultApi with(ProviderInterface provider) {
        val rpc = RpcImpl.with(provider);

        return DefaultApi.with(rpc, GeneratedPalletResolver.with(rpc));
    }

    /**
     * @return the instance that provides a proper API for querying the RPC's methods.
     */
    Rpc rpc();

    /**
     * Resolves the instance of a pallet by its definition.
     * @param clazz the class of the pallet
     * @param <T> the type of the pallet
     * @return appropriate instance of the pallet
     */
    <T> T pallet(Class<T> clazz);
}
