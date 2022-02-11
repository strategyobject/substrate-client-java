package com.strategyobject.substrateclient.rpc.core.decoders;

import com.strategyobject.substrateclient.rpc.core.DecoderPair;
import com.strategyobject.substrateclient.transport.RpcObject;

public class BooleanDecoder extends AbstractDecoder<Boolean> {
    @Override
    protected Boolean decodeNonNull(RpcObject value, DecoderPair<?>[] decoders) {
        return value.asBoolean();
    }
}
