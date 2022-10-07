package com.strategyobject.substrateclient.api.pallet.msa;

import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import com.strategyobject.substrateclient.scale.annotation.ScaleWriter;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@ScaleReader
@ScaleWriter
@Getter
@Setter
public class MessageSourceId {
    @Scale(ScaleType.U64.class)
    private BigInteger value;
}
