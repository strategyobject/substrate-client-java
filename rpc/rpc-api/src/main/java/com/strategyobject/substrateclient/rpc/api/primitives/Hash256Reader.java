package com.strategyobject.substrateclient.rpc.api.primitives;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.io.Streamer;
import com.strategyobject.substrateclient.common.types.Size;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.annotation.AutoRegister;
import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;

@AutoRegister(types = {Hash256.class, Hash.class, BlockHash.class, CallHash.class})
public class Hash256Reader implements ScaleReader<Hash256> {
    @Override
    public Hash256 read(@NonNull InputStream stream, ScaleReader<?>... readers) throws IOException {
        Preconditions.checkArgument(readers == null || readers.length == 0);

        return Hash256.fromBytes(Streamer.readBytes(Size.of32.getValue(), stream));
    }
}
