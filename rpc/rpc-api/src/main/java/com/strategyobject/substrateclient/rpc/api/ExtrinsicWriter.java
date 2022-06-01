package com.strategyobject.substrateclient.rpc.api;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.scale.annotation.AutoRegister;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import lombok.NonNull;
import lombok.val;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@AutoRegister(types = Extrinsic.class)
public class ExtrinsicWriter implements ScaleWriter<Extrinsic<?, ?, ?, ?>> {
    private static final int VERSION = 4;

    @Override
    public void write(@NonNull Extrinsic<?, ?, ?, ?> value, @NonNull OutputStream stream, ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers == null || writers.length == 0);

        val encodedExtrinsic = new ByteArrayOutputStream();
        writeExtrinsic(value, encodedExtrinsic);

        wrapToVec(encodedExtrinsic, stream);
    }

    @SuppressWarnings("unchecked")
    private void wrapToVec(ByteArrayOutputStream encodedExtrinsic, OutputStream stream) throws IOException {
        ((ScaleWriter<Integer>)ScaleWriterRegistry.getInstance().resolve(ScaleType.CompactInteger.class))
                .write(encodedExtrinsic.size(), stream);

        stream.write(encodedExtrinsic.toByteArray());
    }

    @SuppressWarnings("unchecked")
    private void writeExtrinsic(Extrinsic<?, ?, ?, ?> value, ByteArrayOutputStream stream) throws IOException {
        val u8Writer = (ScaleWriter<Integer>) ScaleWriterRegistry.getInstance().resolve(ScaleType.U8.class);
        if (value.getSignature().isPresent()) {
            u8Writer.write(VERSION | 0b1000_0000, stream);
            val signature = (SignaturePayload<?, ?, ?>) value.getSignature().get();
            signature.write(stream);
        } else {
            u8Writer.write(VERSION & 0b0111_1111, stream);
        }

        value.getCall().write(stream);
    }
}
