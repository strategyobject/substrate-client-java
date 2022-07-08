package com.strategyobject.substrateclient.crypto.ss58;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "from")
@EqualsAndHashCode
@Getter
public class AddressWithPrefix {
    private final byte @NonNull [] address;
    private final SS58AddressFormat prefix;
}
