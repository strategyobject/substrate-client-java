package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ListReader<T> implements ScaleReader<List<T>> {
    private final ScaleReader<T> nestedReader;

    @Override
    public List<T> read(@NonNull InputStream stream) throws IOException {
        val len = CompactIntegerReader.readInternal(stream);
        val result = new ArrayList<T>(len);
        for (int i = 0; i < len; i++) {
            result.add(nestedReader.read(stream));
        }

        return result;
    }
}
