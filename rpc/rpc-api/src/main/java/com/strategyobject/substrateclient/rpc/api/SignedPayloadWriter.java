package com.strategyobject.substrateclient.rpc.api;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.ScaleDispatch;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.scale.annotation.AutoRegister;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.io.IOException;
import java.io.OutputStream;

@RequiredArgsConstructor
@AutoRegister(types = SignedPayload.class)
public class SignedPayloadWriter<C extends Call, E extends Extra & SignedExtension> implements ScaleWriter<SignedPayload<C, E>> {
    private final @NonNull ScaleWriterRegistry registry;

    @SuppressWarnings("unchecked")
    @Override
    public void write(@NonNull SignedPayload<C, E> value, @NonNull OutputStream stream, ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers == null || writers.length == 0);

        val dispatcher = registry.resolve(ScaleDispatch.class);
        ((ScaleWriter<C>) dispatcher).write(value.getCall(), stream);
        ((ScaleWriter<E>) dispatcher).write(value.getExtra(), stream);
        ((ScaleWriter<AdditionalExtra>) dispatcher).write(value.getExtra().getAdditionalExtra(), stream);
    }
}
