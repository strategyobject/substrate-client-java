package com.strategyobject.substrateclient.scale.readers.union;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.io.Streamer;
import com.strategyobject.substrateclient.common.types.union.Union;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Function;

public abstract class BaseUnionReader<T extends Union> implements ScaleReader<T> {
    private final int unionSize;
    @SuppressWarnings("rawtypes")
    private final Function[] createUnion;

    @SafeVarargs
    protected BaseUnionReader(int unionSize, Function<?, T>... createUnion) {
        Preconditions.checkArgument(createUnion.length == unionSize);

        this.unionSize = unionSize;
        this.createUnion = createUnion;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T read(@NonNull InputStream stream, ScaleReader<?>... readers) throws IOException {
        Preconditions.checkArgument(readers.length == unionSize);
        Arrays.stream(readers).forEach(Preconditions::checkNotNull);

        val index = Streamer.readByte(stream);
        if (index >= unionSize) {
            throw new NoSuchElementException("Union index is out of bound.");
        }

        return (T) createUnion[index].apply(readers[index].read(stream));
    }
}
