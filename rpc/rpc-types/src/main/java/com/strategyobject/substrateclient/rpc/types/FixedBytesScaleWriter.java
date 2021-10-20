package com.strategyobject.substrateclient.rpc.types;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.io.Streamer;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.scale.annotations.AutoRegister;
import com.strategyobject.substrateclient.types.FixedBytes;
import com.strategyobject.substrateclient.types.Size;
import lombok.NonNull;

import java.io.IOException;
import java.io.OutputStream;

@AutoRegister(types = {AccountId.class, BlockHash.class})
public class FixedBytesScaleWriter implements ScaleWriter<FixedBytes<? extends Size>> {
    @Override
    public void write(@NonNull FixedBytes<? extends Size> value, @NonNull OutputStream stream, ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers == null || writers.length == 0);

        Streamer.writeBytes(value.getData(), stream);
    }
}
