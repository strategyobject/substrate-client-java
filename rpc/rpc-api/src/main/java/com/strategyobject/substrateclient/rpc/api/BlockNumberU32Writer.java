package com.strategyobject.substrateclient.rpc.api;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.scale.annotation.AutoRegister;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import lombok.NonNull;

import java.io.IOException;
import java.io.OutputStream;

@AutoRegister(types = BlockNumber.class)
public class BlockNumberU32Writer implements ScaleWriter<BlockNumber> {
    @SuppressWarnings("unchecked")
    @Override
    public void write(@NonNull BlockNumber value, @NonNull OutputStream stream, ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers == null || writers.length == 0);

        ((ScaleWriter<Long>) ScaleWriterRegistry.getInstance().resolve(ScaleType.U32.class))
                .write(value.getValue().longValue(), stream);
    }
}
