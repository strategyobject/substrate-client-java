package com.strategyobject.substrateclient.rpc.types;

import com.strategyobject.substrateclient.rpc.core.annotations.RpcDecoder;
import com.strategyobject.substrateclient.scale.annotations.Scale;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RpcDecoder
public class Header {
    @Scale
    private BlockHash parentHash;
}
