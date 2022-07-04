package com.strategyobject.substrateclient.rpc.api;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.ScaleDispatch;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.scale.annotation.AutoRegister;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import com.strategyobject.substrateclient.scale.writers.ByteArrayWriter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@AutoRegister(types = Extrinsic.class)
@RequiredArgsConstructor
public class ExtrinsicWriter<C extends Call, A extends Address, S extends Signature, E extends Extra>
        implements ScaleWriter<Extrinsic<C, A, S, E>> {
    private static final int VERSION = 4;

    private final @NonNull ScaleWriterRegistry registry;

    @Override
    public void write(@NonNull Extrinsic<C, A, S, E> value,
                      @NonNull OutputStream stream,
                      ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers == null || writers.length == 0);

        val encodedExtrinsic = new ByteArrayOutputStream();
        writeExtrinsic(value, encodedExtrinsic);
        wrapToVec(encodedExtrinsic, stream);
    }

    private void wrapToVec(ByteArrayOutputStream encodedExtrinsic, OutputStream stream) throws IOException {
        ((ByteArrayWriter) registry.resolve(byte[].class)).write(encodedExtrinsic.toByteArray(), stream);
    }

    @SuppressWarnings("unchecked")
    private void writeExtrinsic(Extrinsic<C, A, S, E> value, ByteArrayOutputStream stream) throws IOException {
        val u8Writer = (ScaleWriter<Integer>) registry.resolve(ScaleType.U8.class);
        val signature = value.getSignature();
        if (signature.isPresent()) {
            u8Writer.write(VERSION | 0b1000_0000, stream);

            val dispatcher = registry.resolve(ScaleDispatch.class);
            val signaturePayloadWriter = (ScaleWriter<SignaturePayload<A, S, E>>) registry.resolve(SignaturePayload.class);
            signaturePayloadWriter
                    .inject(dispatcher, dispatcher, dispatcher)
                    .write(signature.get(), stream);
        } else {
            u8Writer.write(VERSION & 0b0111_1111, stream);
        }

        ((ScaleWriter<C>) registry.resolve(ScaleDispatch.class)).write(value.getCall(), stream);
    }
}
