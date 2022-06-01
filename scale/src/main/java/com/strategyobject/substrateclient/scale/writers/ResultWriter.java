package com.strategyobject.substrateclient.scale.writers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.types.Result;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.OutputStream;

public class ResultWriter implements ScaleWriter<Result<?, ?>> {
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void write(@NonNull Result<?, ?> value, @NonNull OutputStream stream, @NonNull ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers.length == 2);
        Preconditions.checkNotNull(writers[0]);
        Preconditions.checkNotNull(writers[1]);

        val okWriter = (ScaleWriter) writers[0];
        val errWriter = (ScaleWriter) writers[1];
        if (value.isOk()) {
            stream.write(0);
            okWriter.write(value.unwrap(), stream);
        } else {
            stream.write(1);
            errWriter.write(value.unwrapErr(), stream);
        }
    }
}
