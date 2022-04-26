package com.strategyobject.substrateclient.api;

import com.strategyobject.substrateclient.pallet.annotations.Pallet;
import com.strategyobject.substrateclient.pallet.annotations.Storage;
import com.strategyobject.substrateclient.pallet.annotations.StorageHasher;
import com.strategyobject.substrateclient.pallet.annotations.StorageKey;
import com.strategyobject.substrateclient.rpc.types.BlockHash;
import com.strategyobject.substrateclient.scale.annotations.Scale;
import com.strategyobject.substrateclient.storage.StorageNMap;

@Pallet("System")
public interface SystemPallet {
    @Storage(
            value = "BlockHash",
            keys = {
                    @StorageKey(
                            type = @Scale(Integer.class),
                            hasher = StorageHasher.TwoX64Concat
                    )
            })
    StorageNMap<BlockHash> blockHash();
}