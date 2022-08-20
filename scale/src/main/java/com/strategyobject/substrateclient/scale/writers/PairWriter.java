package com.strategyobject.substrateclient.scale.writers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.types.tuple.Pair;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.NonNull;

import java.io.IOException;
import java.io.OutputStream;

public class PairWriter implements ScaleWriter<Pair<?, ?>> {

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void write(@NonNull Pair<?, ?> value, @NonNull OutputStream stream, @NonNull ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers.length == 2);
        Preconditions.checkNotNull(writers[0]);
        Preconditions.checkNotNull(writers[1]);

        ((ScaleWriter) writers[0]).write(value.getValue0(), stream);
        ((ScaleWriter) writers[1]).write(value.getValue1(), stream);
    }
}
