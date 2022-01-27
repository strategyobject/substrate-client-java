package com.strategyobject.substrateclient.rpc.types;

import com.strategyobject.substrateclient.rpc.core.annotations.RpcDecoder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@RpcDecoder
@Getter
@Setter
public class Block {
    private Header header;

    private List<String> extrinsics; // TODO think about a more strict type
}
