package com.strategyobject.substrateclient.scale.writers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.Result;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.OutputStream;

public class ResultWriter<T, E> implements ScaleWriter<Result<T, E>> {
    @Override
    @SuppressWarnings("unchecked")
    public void write(@NonNull Result<T, E> value, @NonNull OutputStream stream, @NonNull ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers.length == 2);
        Preconditions.checkNotNull(writers[0]);
        Preconditions.checkNotNull(writers[1]);

        val okWriter = (ScaleWriter<T>)writers[0];
        val errWriter = (ScaleWriter<E>)writers[1];
        if (value.isOk()) {
            stream.write(0);
            okWriter.write(value.unwrap(), stream);
        } else {
            stream.write(1);
            errWriter.write(value.unwrapErr(), stream);
        }
    }
}
