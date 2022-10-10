package com.strategyobject.substrateclient.api.pallet.vesting;

import com.strategyobject.substrateclient.api.pallet.system.System;
import com.strategyobject.substrateclient.rpc.api.primitives.Balance;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;


@Getter
@Setter
@ScaleReader
public class VestingSchedule {

    @Scale(ScaleType.U32.class)
    private Long start;

    @Scale(ScaleType.U32.class)
    private Long period;

    @Scale(ScaleType.U32.class)
    private Long period_count;

    private Balance per_period; //u128 - type 65 (type 6) - Balance
}