package com.strategyobject.substrateclient.rpc.api;

import com.strategyobject.substrateclient.common.types.FixedBytes;
import com.strategyobject.substrateclient.common.types.Size;
import lombok.NonNull;

public class AccountId extends FixedBytes<Size.Of32> {

    protected AccountId(byte[] data) {
        super(data, Size.of32);
    }

    public static AccountId fromBytes(byte @NonNull [] data) {
        return new AccountId(data);
    }
}
