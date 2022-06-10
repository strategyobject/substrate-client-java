package com.strategyobject.substrateclient.rpc;

import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
@Getter
public class EncoderPair<T> {
    private final RpcEncoder<T> encoder;
    private final ScaleWriter<T> scaleWriter;

    public RpcEncoder<T> getEncoderOrThrow() {
        if (encoder == null) {
            throw new NullPointerException("RpcEncoder is null.");
        }

        return encoder;
    }

    public ScaleWriter<T> getScaleWriterOrThrow() {
        if (scaleWriter == null) {
            throw new NullPointerException("ScaleWriter is null.");
        }

        return scaleWriter;
    }
}
