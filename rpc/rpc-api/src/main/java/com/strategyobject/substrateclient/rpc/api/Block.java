package com.strategyobject.substrateclient.rpc.api;

import com.strategyobject.substrateclient.rpc.annotation.RpcDecoder;
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
