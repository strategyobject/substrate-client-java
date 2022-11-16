package com.strategyobject.substrateclient.rpc.api.primitives;

import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleWriter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

/**
 * Index of a transaction in the chain. Sadly it seems thew Index that is sent in an extrinsic needs to be a CompactBigInteger, however, the SCALE encoding
 * for an Index coming from the AccountNonceApi_account_nonce is a U32 hence the need for 2 different class depending on
 * context
 *
 * @see IndexU32
 */
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

    public IndexU32 toIndexU32(){
        return IndexU32.fromIndex(this);
    }

    public Index fromIndexU32(IndexU32 indexU32) {
        return Index.of(indexU32.getValue());
    }
}
