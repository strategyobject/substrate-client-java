package com.strategyobject.substrateclient.rpc.core.decoders;

import com.strategyobject.substrateclient.rpc.core.DecoderPair;
import com.strategyobject.substrateclient.transport.RpcObject;

public class DoubleDecoder extends AbstractDecoder<Double> {
    @Override
    protected Double decodeNonNull(RpcObject value, DecoderPair<?>[] decoders) {
        return value.asNumber().doubleValue();
    }
}
