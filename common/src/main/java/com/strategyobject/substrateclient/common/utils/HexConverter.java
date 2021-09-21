package com.strategyobject.substrateclient.common.utils;

import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.val;
import lombok.var;

public class HexConverter {
    private static final char[] HEX = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String toHex(byte @NonNull [] data) {
        Preconditions.checkArgument(data.length > 0);

        val hex = new char[(data.length * 2) + 2];
        hex[0] = '0';
        hex[1] = 'x';

        var i = 2;
        for (var b : data) {
            hex[i++] = HEX[(b & 0xf0) >> 4];
            hex[i++] = HEX[b & 0x0f];
        }

        return new String(hex);
    }

    public static byte[] toBytes(@NonNull String hex) {
        Preconditions.checkArgument(
                hex.length() % 2 == 0,
                "Provided string can't be parsed as hex because it has length: %s which is not even.",
                hex.length());

        var charIndex = hex.startsWith("0x") || hex.startsWith("0X") ? 2 : 0;
        val bytes = new byte[(hex.length() - charIndex) / 2];
        for (var i = 0; i < bytes.length; i++) {
            val firstChar = hex.charAt(charIndex++);
            val hi = Character.digit(firstChar, 16);
            if (hi == -1) {
                throw new IllegalArgumentException(String.format("Incorrect symbol %s.", firstChar));
            }

            val secondChar = hex.charAt(charIndex++);
            val low = Character.digit(secondChar, 16);
            if (low == -1) {
                throw new IllegalArgumentException(String.format("Incorrect symbol %s.", secondChar));
            }

            bytes[i] = (byte) ((hi << 4) | low);
        }

        return bytes;
    }
}
