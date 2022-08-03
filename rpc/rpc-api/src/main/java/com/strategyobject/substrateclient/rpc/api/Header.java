package com.strategyobject.substrateclient.rpc.api;

import com.strategyobject.substrateclient.rpc.annotation.RpcDecoder;
import com.strategyobject.substrateclient.rpc.api.primitives.BlockHash;
import com.strategyobject.substrateclient.rpc.api.primitives.BlockNumber;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RpcDecoder
public class Header { // TODO add rest fields
    @Scale
    private BlockHash parentHash;

    private BlockNumber number;
}
