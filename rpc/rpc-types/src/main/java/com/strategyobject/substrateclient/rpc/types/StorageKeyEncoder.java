package com.strategyobject.substrateclient.rpc.types;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.utils.HexConverter;
import com.strategyobject.substrateclient.rpc.core.EncoderPair;
import com.strategyobject.substrateclient.rpc.core.RpcEncoder;
import com.strategyobject.substrateclient.rpc.core.annotations.AutoRegister;
import lombok.NonNull;

@AutoRegister(types = StorageKey.class)
public class StorageKeyEncoder implements RpcEncoder<StorageKey> {
    @Override
    public Object encode(@NonNull StorageKey source, EncoderPair<?>... encoders) {
        Preconditions.checkArgument(encoders == null || encoders.length == 0);

        return HexConverter.toHex(source.getData());
    }
}
