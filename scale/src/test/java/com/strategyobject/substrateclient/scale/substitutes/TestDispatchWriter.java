package com.strategyobject.substrateclient.scale.substitutes;

import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.io.IOException;
import java.io.OutputStream;

@RequiredArgsConstructor
public class TestDispatchWriter implements ScaleWriter<TestDispatch> {
    private final ScaleWriterRegistry scaleWriterRegistry;

    @SuppressWarnings("unchecked")
    @Override
    public void write(@NonNull TestDispatch value, @NonNull OutputStream stream, ScaleWriter<?>... writers) throws IOException {
        val writer = (ScaleWriter<String>) scaleWriterRegistry.resolve(String.class);
        writer.write(value.getValue(), stream);
    }
}
