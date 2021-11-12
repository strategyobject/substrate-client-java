package com.strategyobject.substrateclient.rpc.core.decoders;

import com.strategyobject.substrateclient.rpc.core.DecoderPair;

public class ByteDecoder extends AbstractDecoder<Byte> {
    @Override
    protected Byte decodeNonNull(Object value, DecoderPair<?>[] decoders) {
        return ((Double) value).byteValue();
    }
}
