package com.strategyobject.substrateclient.rpc.api.primitives;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

/**
 * Index of an account.
 */
@RequiredArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
public class AccountIndex {
    public static final AccountIndex ZERO = AccountIndex.of(BigInteger.ZERO);

    private final BigInteger value;

    public static AccountIndex of(long value) {
        return of(BigInteger.valueOf(value));
    }
}
