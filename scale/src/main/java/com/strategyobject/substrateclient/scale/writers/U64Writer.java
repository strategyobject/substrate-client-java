package com.strategyobject.substrateclient.scale.writers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.val;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

public class U64Writer implements ScaleWriter<BigInteger> {
    @Override
    public void write(BigInteger value, OutputStream stream) throws IOException {
        Preconditions.checkArgument(
                value.compareTo(BigInteger.valueOf(0)) >= 0 && value.compareTo(BigInteger.ZERO.setBit(64)) < 0,
                "Only values in range 0..2^64-1 are supported");

        val mask = BigInteger.valueOf(0xff);
        stream.write(value.and(mask).intValue());
        stream.write(value.shiftRight(8).and(mask).intValue());
        stream.write(value.shiftRight(16).and(mask).intValue());
        stream.write(value.shiftRight(24).and(mask).intValue());
        stream.write(value.shiftRight(32).and(mask).intValue());
        stream.write(value.shiftRight(40).and(mask).intValue());
        stream.write(value.shiftRight(48).and(mask).intValue());
        stream.write(value.shiftRight(56).and(mask).intValue());
    }
}
