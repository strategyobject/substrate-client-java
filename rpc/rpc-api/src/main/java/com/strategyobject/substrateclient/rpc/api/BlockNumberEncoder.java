package com.strategyobject.substrateclient.rpc.api;

import com.strategyobject.substrateclient.rpc.EncoderPair;
import com.strategyobject.substrateclient.rpc.RpcEncoder;
import com.strategyobject.substrateclient.rpc.annotation.AutoRegister;

@AutoRegister(types = BlockNumber.class)
public class BlockNumberEncoder implements RpcEncoder<BlockNumber> {

    @Override
    public Object encode(BlockNumber source, EncoderPair<?>... encoders) {
        return "0x" + source.getValue().toString(16);
    }
}
