package com.strategyobject.substrateclient.rpc.core.decoders;

import com.strategyobject.substrateclient.rpc.core.DecoderPair;
import com.strategyobject.substrateclient.transport.RpcObject;

public class FloatDecoder extends AbstractDecoder<Float> {
    @Override
    protected Float decodeNonNull(RpcObject value, DecoderPair<?>[] decoders) {
        return value.asNumber().floatValue();
    }
}
