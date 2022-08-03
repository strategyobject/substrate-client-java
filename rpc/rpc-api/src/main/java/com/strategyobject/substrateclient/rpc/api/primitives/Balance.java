package com.strategyobject.substrateclient.rpc.api.primitives;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

/**
 * Balance of an account.
 */
@RequiredArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
public class Balance {
    public static final Balance ZERO = Balance.of(BigInteger.ZERO);

    private final BigInteger value;
}
