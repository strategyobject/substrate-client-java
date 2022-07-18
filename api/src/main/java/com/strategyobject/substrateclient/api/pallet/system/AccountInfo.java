package com.strategyobject.substrateclient.api.pallet.system;

import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ScaleReader
public
class AccountInfo {
    @Scale(ScaleType.U32.class)
    private Long nonce;

    @Scale(ScaleType.U32.class)
    private Long consumers;

    @Scale(ScaleType.U32.class)
    private Long providers;

    private System.AccountData data;
}
