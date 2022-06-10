package com.strategyobject.substrateclient.rpc;

import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
@Getter
public class DecoderPair<T> {
    private final RpcDecoder<T> decoder;
    private final ScaleReader<T> scaleReader;

    public RpcDecoder<T> getDecoderOrThrow() {
        if (decoder == null) {
            throw new NullPointerException("RpcDecoder is null.");
        }

        return decoder;
    }

    public ScaleReader<T> getScaleReaderOrThrow() {
        if (scaleReader == null) {
            throw new NullPointerException("ScaleReader is null.");
        }

        return scaleReader;
    }
}
