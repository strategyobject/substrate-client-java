package com.strategyobject.substrateclient.rpc.core.decoders;

import com.strategyobject.substrateclient.rpc.core.DecoderPair;
import com.strategyobject.substrateclient.transport.RpcObject;

public class ByteDecoder extends AbstractDecoder<Byte> {
    @Override
    protected Byte decodeNonNull(RpcObject value, DecoderPair<?>[] decoders) {
        return value.asNumber().byteValue();
    }
}
