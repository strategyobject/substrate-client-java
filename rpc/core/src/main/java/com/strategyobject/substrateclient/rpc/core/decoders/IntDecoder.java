package com.strategyobject.substrateclient.rpc.core.decoders;

import com.strategyobject.substrateclient.rpc.core.DecoderPair;

public class IntDecoder extends AbstractDecoder<Integer> {
    @Override
    protected Integer decodeNonNull(Object value, DecoderPair<?>[] decoders) {
        return ((Double) value).intValue();
    }
}
