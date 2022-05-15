package com.strategyobject.substrateclient.scale.readers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.io.Streamer;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;
import lombok.val;
import lombok.var;

import java.io.IOException;
import java.io.InputStream;

public class BooleanArrayReader implements ScaleReader<boolean[]> {
    @Override
    public boolean[] read(@NonNull InputStream stream, ScaleReader<?>... readers) throws IOException {
        Preconditions.checkArgument(readers == null || readers.length == 0);

        val len = CompactIntegerReader.readInternal(stream);
        val result = new boolean[len];
        val src = Streamer.readBytes(len, stream);
        for (var i = 0; i < len; i++) {
            result[i] = src[i] != 0;
        }

        return result;
    }
}
