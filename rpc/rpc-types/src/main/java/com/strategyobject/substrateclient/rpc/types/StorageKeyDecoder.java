package com.strategyobject.substrateclient.rpc.types;

import com.strategyobject.substrateclient.common.utils.HexConverter;
import com.strategyobject.substrateclient.rpc.core.DecoderPair;
import com.strategyobject.substrateclient.rpc.core.annotations.AutoRegister;
import com.strategyobject.substrateclient.rpc.core.decoders.AbstractDecoder;
import com.strategyobject.substrateclient.transport.RpcObject;

@AutoRegister(types = StorageKey.class)
public class StorageKeyDecoder extends AbstractDecoder<StorageKey> {
    @Override
    protected StorageKey decodeNonNull(RpcObject value, DecoderPair<?>[] decoders) {
        return StorageKey.valueOf(HexConverter.toBytes(value.asString()));
    }
}
