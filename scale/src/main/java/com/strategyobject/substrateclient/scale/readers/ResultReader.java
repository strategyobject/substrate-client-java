package com.strategyobject.substrateclient.scale.readers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.io.Streamer;
import com.strategyobject.substrateclient.common.types.Result;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.InputStream;

public class ResultReader implements ScaleReader<Result<?, ?>> {
    @Override
    public Result<?, ?> read(@NonNull InputStream stream, @NonNull ScaleReader<?>... readers) throws IOException {
        Preconditions.checkArgument(readers.length == 2);
        Preconditions.checkNotNull(readers[0]);
        Preconditions.checkNotNull(readers[1]);

        val okReader = readers[0];
        val errReader = readers[1];
        return Streamer.readByte(stream) == 0
                ? Result.ok(okReader.read(stream))
                : Result.err(errReader.read(stream));
    }
}
