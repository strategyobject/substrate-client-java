package com.strategyobject.substrateclient.scale.readers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.types.tuple.Pair;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;

public class PairReader implements ScaleReader<Pair<?, ?>> {

    @Override
    public Pair<?, ?> read(@NonNull InputStream stream, @NonNull ScaleReader<?>... readers) throws IOException {
        Preconditions.checkArgument(readers.length == 2);
        Preconditions.checkNotNull(readers[0]);
        Preconditions.checkNotNull(readers[1]);

        return Pair.of(
                readers[0].read(stream),
                readers[1].read(stream));
    }
}
