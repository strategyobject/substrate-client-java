package com.strategyobject.substrateclient.rpc.api;

import com.strategyobject.substrateclient.common.types.FixedBytes;
import com.strategyobject.substrateclient.common.types.Size;
import com.strategyobject.substrateclient.crypto.ss58.SS58Codec;
import lombok.NonNull;
import lombok.var;

import java.util.concurrent.atomic.AtomicReference;

public class AccountId extends FixedBytes<Size.Of32> {

    private final AtomicReference<String> encoded = new AtomicReference<>(null);

    protected AccountId(byte[] data) {
        super(data, Size.of32);
    }

    @Override
    public String toString() {
        var result = encoded.get();
        if (result != null) {
            return result;
        }

        result = SS58Codec.encode(getBytes(), MetadataRegistry.getInstance().getSS58AddressFormat());
        return encoded.compareAndSet(null, result) ? result : encoded.get();
    }

    public static AccountId fromBytes(byte @NonNull [] data) {
        return new AccountId(data);
    }
}
