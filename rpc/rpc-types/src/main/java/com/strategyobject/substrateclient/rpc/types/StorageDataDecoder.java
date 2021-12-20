package com.strategyobject.substrateclient.rpc.types;

import com.strategyobject.substrateclient.common.utils.HexConverter;
import com.strategyobject.substrateclient.rpc.core.DecoderPair;
import com.strategyobject.substrateclient.rpc.core.annotations.AutoRegister;
import com.strategyobject.substrateclient.rpc.core.decoders.AbstractDecoder;

@AutoRegister(types = StorageData.class)
public class StorageDataDecoder extends AbstractDecoder<StorageData> {
    @Override
    protected StorageData decodeNonNull(Object value, DecoderPair<?>[] decoders) {
        return StorageData.valueOf(HexConverter.toBytes((String) value));
    }
}