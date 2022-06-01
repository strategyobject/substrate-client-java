package com.strategyobject.substrateclient.rpc.decoders;

import com.strategyobject.substrateclient.rpc.DecoderPair;
import com.strategyobject.substrateclient.transport.RpcObject;

public class IntDecoder extends AbstractDecoder<Integer> {
    @Override
    protected Integer decodeNonNull(RpcObject value, DecoderPair<?>[] decoders) {
        return value.asNumber().intValue();
    }
}
