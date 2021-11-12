package com.strategyobject.substrateclient.rpc.core.decoders;

import com.strategyobject.substrateclient.rpc.core.DecoderPair;

public class LongDecoder extends AbstractDecoder<Long> {
    @Override
    protected Long decodeNonNull(Object value, DecoderPair<?>[] decoders) {
        return ((Double) value).longValue();
    }
}
