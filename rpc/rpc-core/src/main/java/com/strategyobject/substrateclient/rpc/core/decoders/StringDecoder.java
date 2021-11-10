package com.strategyobject.substrateclient.rpc.core.decoders;

import com.strategyobject.substrateclient.rpc.core.DecoderPair;

public class StringDecoder extends AbstractDecoder<String> {
    @Override
    protected String decodeNonNull(Object value, DecoderPair<?>[] decoders) {
        return (String) value;
    }
}
