package com.strategyobject.substrateclient.rpc.api;

import com.strategyobject.substrateclient.common.convert.HexConverter;
import com.strategyobject.substrateclient.rpc.DecoderPair;
import com.strategyobject.substrateclient.rpc.annotation.AutoRegister;
import com.strategyobject.substrateclient.rpc.decoders.AbstractDecoder;
import com.strategyobject.substrateclient.transport.RpcObject;

@AutoRegister(types = StorageKey.class)
public class StorageKeyDecoder extends AbstractDecoder<StorageKey> {
    @Override
    protected StorageKey decodeNonNull(RpcObject value, DecoderPair<?>[] decoders) {
        return StorageKey.valueOf(HexConverter.toBytes(value.asString()));
    }
}
