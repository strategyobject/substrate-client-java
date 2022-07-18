package com.strategyobject.substrateclient.rpc.api.storage;

import com.strategyobject.substrateclient.common.convert.HexConverter;
import com.strategyobject.substrateclient.rpc.DecoderPair;
import com.strategyobject.substrateclient.rpc.annotation.AutoRegister;
import com.strategyobject.substrateclient.rpc.decoders.AbstractDecoder;
import com.strategyobject.substrateclient.transport.RpcObject;

@AutoRegister(types = StorageData.class)
public class StorageDataDecoder extends AbstractDecoder<StorageData> {
    @Override
    protected StorageData decodeNonNull(RpcObject value, DecoderPair<?>[] decoders) {
        return StorageData.valueOf(HexConverter.toBytes(value.asString()));
    }
}