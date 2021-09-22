package com.strategyobject.substrateclient.scale.readers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.streams.StreamUtils;
import com.strategyobject.substrateclient.scale.Result;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.InputStream;

public class ResultReader<T, E> implements ScaleReader<Result<T, E>> {
    @Override
    @SuppressWarnings("unchecked")
    public Result<T, E> read(@NonNull InputStream stream, @NonNull ScaleReader<?>... readers) throws IOException {
        Preconditions.checkArgument(readers.length == 2);
        Preconditions.checkNotNull(readers[0]);
        Preconditions.checkNotNull(readers[1]);

        val okReader = (ScaleReader<T>)readers[0];
        val errReader = (ScaleReader<E>)readers[1];
        return StreamUtils.readByte(stream) == 0
                ? Result.ok(okReader.read(stream))
                : Result.err(errReader.read(stream));
    }
}
