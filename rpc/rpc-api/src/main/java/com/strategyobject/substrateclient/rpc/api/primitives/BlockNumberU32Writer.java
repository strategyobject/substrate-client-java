package com.strategyobject.substrateclient.rpc.api.primitives;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.scale.annotation.AutoRegister;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;

@AutoRegister(types = BlockNumber.class)
@RequiredArgsConstructor
public class BlockNumberU32Writer implements ScaleWriter<BlockNumber> {
    private final @NonNull ScaleWriterRegistry registry;

    @SuppressWarnings("unchecked")
    @Override
    public void write(@NonNull BlockNumber value, @NonNull OutputStream stream, ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers == null || writers.length == 0);

        ((ScaleWriter<Long>) registry.resolve(ScaleType.U32.class)).write(value.getValue().longValue(), stream);
    }
}
