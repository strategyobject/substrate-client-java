package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@RequiredArgsConstructor
public class ListWriter<T> implements ScaleWriter<List<T>> {
    private final ScaleWriter<T> nestedWriter;

    @Override
    public void write(List<T> value, OutputStream stream) throws IOException {
        CompactIntegerWriter.writeInternal(value.size(), stream);
        for (T item : value) {
            nestedWriter.write(item, stream);
        }
    }
}
