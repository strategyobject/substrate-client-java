package com.strategyobject.substrateclient.rpc.types;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.io.Streamer;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.annotations.AutoRegister;
import com.strategyobject.substrateclient.types.Size;
import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;

@AutoRegister(types = BlockHash.class)
public class BlockHashScaleReader implements ScaleReader<BlockHash> {
    @Override
    public BlockHash read(@NonNull InputStream stream, ScaleReader<?>... readers) throws IOException {
        Preconditions.checkArgument(readers == null || readers.length == 0);

        return BlockHash.fromBytes(Streamer.readBytes(Size.of32.getValue(), stream));
    }
}
