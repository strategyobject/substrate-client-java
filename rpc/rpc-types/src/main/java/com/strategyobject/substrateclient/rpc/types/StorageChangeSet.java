package com.strategyobject.substrateclient.rpc.types;

import com.strategyobject.substrateclient.rpc.core.annotations.RpcDecoder;
import com.strategyobject.substrateclient.scale.annotations.Scale;
import com.strategyobject.substrateclient.types.tuples.Pair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@RpcDecoder
@NoArgsConstructor
@Getter
@Setter
public class StorageChangeSet {
    @Scale
    private BlockHash block;
    private List<Pair<StorageKey, StorageData>> changes;
}
