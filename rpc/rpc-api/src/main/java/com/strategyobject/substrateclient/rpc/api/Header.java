package com.strategyobject.substrateclient.rpc.api;

import com.strategyobject.substrateclient.rpc.annotation.RpcDecoder;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RpcDecoder
public class Header { // TODO add rest fields
    @Scale
    private BlockHash parentHash;

    private Number number; /* TODO probably it would be better to change the type to BigInteger
                                and support specific decoders */
}
