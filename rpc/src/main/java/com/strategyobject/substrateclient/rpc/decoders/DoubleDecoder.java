package com.strategyobject.substrateclient.rpc.decoders;

import com.strategyobject.substrateclient.rpc.DecoderPair;
import com.strategyobject.substrateclient.transport.RpcObject;

public class DoubleDecoder extends AbstractDecoder<Double> {
    @Override
    protected Double decodeNonNull(RpcObject value, DecoderPair<?>[] decoders) {
        return value.asNumber().doubleValue();
    }
}
