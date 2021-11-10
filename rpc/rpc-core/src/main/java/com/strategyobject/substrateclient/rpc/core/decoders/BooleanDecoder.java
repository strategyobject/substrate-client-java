package com.strategyobject.substrateclient.rpc.core.decoders;

import com.strategyobject.substrateclient.rpc.core.DecoderPair;

public class BooleanDecoder extends AbstractDecoder<Boolean> {
    @Override
    protected Boolean decodeNonNull(Object value, DecoderPair<?>[] decoders) {
        return (Boolean) value;
    }
}
