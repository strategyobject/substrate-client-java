package com.strategyobject.substrateclient.scale.writers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.io.Streamer;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.types.PublicKey;
import lombok.NonNull;

import java.io.IOException;
import java.io.OutputStream;

public class PublicKeyWriter implements ScaleWriter<PublicKey> {
    @Override
    public void write(@NonNull PublicKey value, @NonNull OutputStream stream, ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers == null || writers.length == 0);

        Streamer.writeBytes(value.getData(), stream);
    }
}