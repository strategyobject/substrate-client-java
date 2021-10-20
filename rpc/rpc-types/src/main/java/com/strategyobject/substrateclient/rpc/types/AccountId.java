package com.strategyobject.substrateclient.rpc.types;

import com.strategyobject.substrateclient.scale.ScaleSelfWritable;
import com.strategyobject.substrateclient.types.FixedBytes;
import com.strategyobject.substrateclient.types.Size;
import lombok.NonNull;

public class AccountId
        extends FixedBytes<Size.Of32>
        implements ScaleSelfWritable<AccountId> {

    protected AccountId(byte[] data) {
        super(data, Size.of32);
    }

    public static AccountId fromBytes(byte @NonNull [] data) {
        return new AccountId(data);
    }
}
