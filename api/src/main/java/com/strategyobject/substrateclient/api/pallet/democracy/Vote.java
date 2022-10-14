package com.strategyobject.substrateclient.api.pallet.democracy;

import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

@ScaleReader
@Getter
@Setter
public class Vote {
    @Scale(ScaleType.U8.class)
    private Integer vote;
}