package com.strategyobject.substrateclient.rpc.api;

import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleWriter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@RequiredArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ScaleWriter
public class Index {
    public static final Index ZERO = Index.of(BigInteger.ZERO);

    @Scale(ScaleType.CompactBigInteger.class)
    private final BigInteger value;

    public static Index of(long value) {
        return of(BigInteger.valueOf(value));
    }
}
