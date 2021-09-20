package com.strategyobject.substrateclient.scale;

import com.google.common.base.Preconditions;
import lombok.Getter;

import java.math.BigInteger;

@Getter
public enum CompactMode {
    SINGLE((byte) 0b00),
    TWO((byte) 0b01),
    FOUR((byte) 0b10),
    BIG_INTEGER((byte) 0b11);

    private final byte value;

    CompactMode(byte value) {
        this.value = value;
    }

    private static final BigInteger MAX_NUMBER = BigInteger.valueOf(2).pow(536).subtract(BigInteger.ONE);

    public static CompactMode fromValue(byte value) {
        if (value == SINGLE.value) {
            return SINGLE;
        } else if (value == TWO.value) {
            return TWO;
        } else if (value == FOUR.value) {
            return FOUR;
        } else {
            return BIG_INTEGER;
        }
    }

    public static CompactMode fromNumber(int number) {
        Preconditions.checkArgument(
                number >= 0,
                "Negative numbers are not supported");

        if (number <= 0x3f) {
            return CompactMode.SINGLE;
        } else if (number <= 0x3f_ff) {
            return CompactMode.TWO;
        } else if (number <= 0x3f_ff_ff_ff) {
            return CompactMode.FOUR;
        } else {
            return CompactMode.BIG_INTEGER;
        }
    }

    public static CompactMode fromNumber(BigInteger number) {
        Preconditions.checkArgument(number.signum() >= 0, "Negative numbers are not supported");
        Preconditions.checkArgument(
                number.compareTo(MAX_NUMBER) <= 0,
                "Numbers larger than 2^536-1 are not supported");

        if (number.compareTo(BigInteger.valueOf(0x3f)) <= 0) {
            return CompactMode.SINGLE;
        } else if (number.compareTo(BigInteger.valueOf(0x3f_ff)) <= 0) {
            return CompactMode.TWO;
        } else if (number.compareTo(BigInteger.valueOf(0x3f_ff_ff_ff)) <= 0) {
            return CompactMode.FOUR;
        } else {
            return CompactMode.BIG_INTEGER;
        }
    }
}
