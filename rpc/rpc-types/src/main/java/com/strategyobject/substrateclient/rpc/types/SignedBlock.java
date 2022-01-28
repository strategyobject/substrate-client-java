package com.strategyobject.substrateclient.rpc.types;

import com.strategyobject.substrateclient.rpc.core.annotations.RpcDecoder;
import lombok.Getter;
import lombok.Setter;

@RpcDecoder
@Getter
@Setter
public class SignedBlock { // TODO add rest fields
    private Block block;
}
