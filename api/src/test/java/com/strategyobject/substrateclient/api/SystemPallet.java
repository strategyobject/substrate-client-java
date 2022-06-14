package com.strategyobject.substrateclient.api;

import com.strategyobject.substrateclient.pallet.annotation.Pallet;
import com.strategyobject.substrateclient.pallet.annotation.Storage;
import com.strategyobject.substrateclient.pallet.annotation.StorageHasher;
import com.strategyobject.substrateclient.pallet.annotation.StorageKey;
import com.strategyobject.substrateclient.pallet.storage.StorageNMap;
import com.strategyobject.substrateclient.rpc.api.BlockHash;
import com.strategyobject.substrateclient.rpc.api.BlockNumber;
import com.strategyobject.substrateclient.scale.annotation.Scale;

@Pallet("System")
public interface SystemPallet {
    @Storage(
            value = "BlockHash",
            keys = {
                    @StorageKey(
                            type = @Scale(BlockNumber.class),
                            hasher = StorageHasher.TWOX_64_CONCAT
                    )
            })
    StorageNMap<BlockHash> blockHash();
}