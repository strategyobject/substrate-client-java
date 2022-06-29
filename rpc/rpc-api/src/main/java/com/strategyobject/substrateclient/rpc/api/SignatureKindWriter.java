package com.strategyobject.substrateclient.rpc.api;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.scale.annotation.AutoRegister;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;

@AutoRegister(types = SignatureKind.class)
@RequiredArgsConstructor
public class SignatureKindWriter implements ScaleWriter<SignatureKind> {
    private final @NonNull ScaleWriterRegistry registry;

    @Override
    @SuppressWarnings("unchecked")
    public void write(@NonNull SignatureKind value, @NonNull OutputStream stream, ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers == null || writers.length == 0);

        ((ScaleWriter<Byte>) registry.resolve(byte.class)).write(value.getValue(), stream);
    }
}
