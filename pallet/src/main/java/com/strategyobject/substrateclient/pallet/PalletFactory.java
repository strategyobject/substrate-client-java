package com.strategyobject.substrateclient.pallet;

public interface PalletFactory {
    <T> T create(Class<T> clazz);
}
