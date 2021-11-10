package com.strategyobject.substrateclient.rpc.core.decoders;

import com.strategyobject.substrateclient.rpc.core.DecoderPair;

public class DoubleDecoder extends AbstractDecoder<Double> {
    @Override
    protected Double decodeNonNull(Object value, DecoderPair<?>[] decoders) {
        return (Double) value;
    }
}
