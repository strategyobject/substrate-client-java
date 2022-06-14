package com.strategyobject.substrateclient.rpc.api;

import com.strategyobject.substrateclient.scale.ScaleSelfWritable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@RequiredArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
public class BlockNumber implements ScaleSelfWritable<BlockNumber> {
    public static final BlockNumber GENESIS = BlockNumber.of(BigInteger.ZERO);

    private final BigInteger value;

    public static BlockNumber of(long value) {
        return of(BigInteger.valueOf(value));
    }
}
