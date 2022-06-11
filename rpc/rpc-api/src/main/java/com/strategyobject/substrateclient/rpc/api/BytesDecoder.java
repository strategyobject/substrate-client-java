package com.strategyobject.substrateclient.rpc.api;

import com.strategyobject.substrateclient.common.types.Bytes;
import com.strategyobject.substrateclient.common.utils.HexConverter;
import com.strategyobject.substrateclient.rpc.DecoderPair;
import com.strategyobject.substrateclient.rpc.annotation.AutoRegister;
import com.strategyobject.substrateclient.rpc.decoders.AbstractDecoder;
import com.strategyobject.substrateclient.transport.RpcObject;

@AutoRegister(types = Bytes.class)
public class BytesDecoder extends AbstractDecoder<Bytes> {
    @Override
    protected Bytes decodeNonNull(RpcObject value, DecoderPair<?>[] decoders) {
        return () -> HexConverter.toBytes(value.asString());
    }
}
