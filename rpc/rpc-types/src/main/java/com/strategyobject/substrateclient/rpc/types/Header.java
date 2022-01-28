package com.strategyobject.substrateclient.rpc.types;

import com.strategyobject.substrateclient.rpc.core.annotations.RpcDecoder;
import com.strategyobject.substrateclient.scale.annotations.Scale;
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
