package com.strategyobject.substrateclient.scale.readers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.InputStream;

public class ArrayReader implements ScaleReader<Object[]> {
    @Override
    public Object[] read(@NonNull InputStream stream, @NonNull ScaleReader<?>... readers) throws IOException {
        Preconditions.checkArgument(readers.length == 1);
        Preconditions.checkNotNull(readers[0]);

        val len = CompactIntegerReader.readInternal(stream);
        val result = new Object[len];
        for (int i = 0; i < len; i++) {
            result[i] = readers[0].read(stream);
        }

        return result;
    }
}
