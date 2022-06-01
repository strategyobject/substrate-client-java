package com.strategyobject.substrateclient.rpc.api;

import com.strategyobject.substrateclient.rpc.annotation.RpcDecoder;
import lombok.Getter;
import lombok.Setter;

@RpcDecoder
@Getter
@Setter
public class SignedBlock { // TODO add rest fields
    private Block block;
}
