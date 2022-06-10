package com.strategyobject.substrateclient.rpc.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@RequiredArgsConstructor(staticName = "of")
@Getter
public class Number {
    private final BigInteger value;
}
