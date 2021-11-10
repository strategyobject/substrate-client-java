package com.strategyobject.substrateclient.rpc.core.decoders;

import com.strategyobject.substrateclient.rpc.core.DecoderPair;

public class ShortDecoder extends AbstractDecoder<Short> {
    @Override
    protected Short decodeNonNull(Object value, DecoderPair<?>[] decoders) {
        return ((Double) value).shortValue();
    }
}
