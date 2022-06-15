package com.strategyobject.substrateclient.rpc.api;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.scale.annotation.AutoRegister;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import lombok.NonNull;

import java.io.IOException;
import java.io.OutputStream;

@AutoRegister(types = SignatureKind.class)
public class SignatureKindWriter implements ScaleWriter<SignatureKind> {
    @Override
    @SuppressWarnings("unchecked")
    public void write(@NonNull SignatureKind value, @NonNull OutputStream stream, ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers == null || writers.length == 0);

        ((ScaleWriter<Byte>) ScaleWriterRegistry.resolve(byte.class)).write(value.getValue(), stream);
    }
}
