package com.strategyobject.substrateclient.api;

import com.strategyobject.substrateclient.rpc.api.Call;
import com.strategyobject.substrateclient.scale.annotation.ScaleWriter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@ScaleWriter
public class CreateMsa implements Call {
    private final byte moduleIndex;
    private final byte callIndex;
}
