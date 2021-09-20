package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.scale.Result;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;

@RequiredArgsConstructor
public class ResultWriter<T, E> implements ScaleWriter<Result<T, E>> {
    private final ScaleWriter<T> okWriter;
    private final ScaleWriter<E> errWriter;

    @Override
    public void write(Result<T, E> value, OutputStream stream) throws IOException {
        if (value.isOk()) {
            stream.write(0);
            okWriter.write(value.unwrap(), stream);
        } else {
            stream.write(1);
            errWriter.write(value.unwrapErr(), stream);
        }
    }
}
