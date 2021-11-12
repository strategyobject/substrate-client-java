package com.strategyobject.substrateclient.rpc.core.decoders;

import com.strategyobject.substrateclient.rpc.core.DecoderPair;

public class FloatDecoder extends AbstractDecoder<Float> {
    @Override
    protected Float decodeNonNull(Object value, DecoderPair<?>[] decoders) {
        return ((Double) value).floatValue();
    }
}
