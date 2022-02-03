package com.strategyobject.substrateclient.rpc.types;

import com.strategyobject.substrateclient.crypto.ss58.SS58Codec;
import com.strategyobject.substrateclient.rpc.core.MetadataRegistry;
import com.strategyobject.substrateclient.scale.ScaleSelfWritable;
import com.strategyobject.substrateclient.types.FixedBytes;
import com.strategyobject.substrateclient.types.Size;
import lombok.NonNull;
import lombok.var;

import java.util.concurrent.atomic.AtomicReference;

public class AccountId
        extends FixedBytes<Size.Of32>
        implements ScaleSelfWritable<AccountId> {

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

        result = SS58Codec.encode(getData(), MetadataRegistry.getInstance().getSS58AddressFormat());
        return encoded.compareAndSet(null, result) ? result : encoded.get();
    }

    public static AccountId fromBytes(byte @NonNull [] data) {
        return new AccountId(data);
    }
}
