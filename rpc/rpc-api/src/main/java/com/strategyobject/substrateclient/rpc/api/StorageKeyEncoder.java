package com.strategyobject.substrateclient.rpc.api;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.utils.HexConverter;
import com.strategyobject.substrateclient.rpc.EncoderPair;
import com.strategyobject.substrateclient.rpc.RpcEncoder;
import com.strategyobject.substrateclient.rpc.annotation.AutoRegister;
import lombok.NonNull;

@AutoRegister(types = StorageKey.class)
public class StorageKeyEncoder implements RpcEncoder<StorageKey> {
    @Override
    public Object encode(@NonNull StorageKey source, EncoderPair<?>... encoders) {
        Preconditions.checkArgument(encoders == null || encoders.length == 0);

        return HexConverter.toHex(source.getData());
    }
}
