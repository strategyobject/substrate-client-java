package com.strategyobject.substrateclient.rpc.decoders;

import com.strategyobject.substrateclient.rpc.DecoderPair;
import com.strategyobject.substrateclient.transport.RpcObject;

public class StringDecoder extends AbstractDecoder<String> {
    @Override
    protected String decodeNonNull(RpcObject value, DecoderPair<?>[] decoders) {
        return value.asString();
    }
}
