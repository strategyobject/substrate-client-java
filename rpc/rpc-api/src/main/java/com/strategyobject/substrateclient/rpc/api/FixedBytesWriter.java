package com.strategyobject.substrateclient.rpc.api;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.io.Streamer;
import com.strategyobject.substrateclient.common.types.FixedBytes;
import com.strategyobject.substrateclient.common.types.Size;
import com.strategyobject.substrateclient.crypto.PublicKey;
import com.strategyobject.substrateclient.crypto.SignatureData;
import com.strategyobject.substrateclient.rpc.api.impl.Hash256;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.scale.annotation.AutoRegister;
import lombok.NonNull;

import java.io.IOException;
import java.io.OutputStream;

@AutoRegister(
        types = {
                AccountId.class,
                Hash256.class,
                Hash.class,
                BlockHash.class,
                PublicKey.class,
                SignatureData.class
        })
public class FixedBytesWriter implements ScaleWriter<FixedBytes<? extends Size>> {
    @Override
    public void write(@NonNull FixedBytes<? extends Size> value,
                      @NonNull OutputStream stream,
                      ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers == null || writers.length == 0);

        Streamer.writeBytes(value.getBytes(), stream);
    }
}
