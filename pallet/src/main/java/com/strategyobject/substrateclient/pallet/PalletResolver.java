package com.strategyobject.substrateclient.pallet;

public interface PalletResolver {
    <T> T resolve(Class<T> clazz);
}
