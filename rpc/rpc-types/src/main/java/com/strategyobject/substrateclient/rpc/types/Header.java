package com.strategyobject.substrateclient.rpc.types;

import com.strategyobject.substrateclient.rpc.core.RpcEncoded;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Header implements RpcEncoded<Header> {
    private final BlockHash parentHash;
}
