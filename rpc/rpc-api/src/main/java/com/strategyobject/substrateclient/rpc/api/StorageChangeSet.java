package com.strategyobject.substrateclient.rpc.api;

import com.strategyobject.substrateclient.common.types.tuple.Pair;
import com.strategyobject.substrateclient.rpc.annotation.RpcDecoder;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@RpcDecoder
@Getter
@Setter
public class StorageChangeSet {
    @Scale
    private BlockHash block;
    private List<Pair<StorageKey, StorageData>> changes;
}
