package com.strategyobject.substrateclient.rpc.types;

import com.strategyobject.substrateclient.scale.annotations.ScaleWriter;
import lombok.Getter;
import lombok.NonNull;

@Getter
@ScaleWriter
public class AddressId implements Address {
    private final AddressKind kind = AddressKind.ID;
    private final AccountId address;

    private AddressId(@NonNull AccountId address) {
        this.address = address;
    }

    public static AddressId fromBytes(byte @NonNull [] accountId) {
        return new AddressId(AccountId.fromBytes(accountId));
    }
}
