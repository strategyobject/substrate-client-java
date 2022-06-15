package com.strategyobject.substrateclient.rpc.api;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.AutoRegister;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.InputStream;

@AutoRegister(types = BlockNumber.class)
public class BlockNumberU32Reader implements ScaleReader<BlockNumber> {
    @SuppressWarnings("unchecked")
    @Override
    public BlockNumber read(@NonNull InputStream stream, ScaleReader<?>... readers) throws IOException {
        Preconditions.checkArgument(readers == null || readers.length == 0);

        val u32Reader = (ScaleReader<Long>) ScaleReaderRegistry.resolve(ScaleType.U32.class);
        return BlockNumber.of(u32Reader.read(stream));
    }
}
