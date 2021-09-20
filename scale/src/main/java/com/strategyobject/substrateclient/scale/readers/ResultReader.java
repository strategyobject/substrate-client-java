package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.common.streams.StreamUtils;
import com.strategyobject.substrateclient.scale.Result;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
public class ResultReader<T, E> implements ScaleReader<Result<T, E>> {
    private final ScaleReader<T> okReader;
    private final ScaleReader<E> errReader;

    @Override
    public Result<T, E> read(@NonNull InputStream stream) throws IOException {
        return StreamUtils.readByte(stream) == 0
                ? Result.ok(okReader.read(stream))
                : Result.err(errReader.read(stream));
    }
}
