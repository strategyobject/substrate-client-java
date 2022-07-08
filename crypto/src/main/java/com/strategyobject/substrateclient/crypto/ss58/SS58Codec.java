package com.strategyobject.substrateclient.crypto.ss58;

import com.google.common.base.Preconditions;
import io.ipfs.multibase.Base58;
import lombok.NonNull;
import lombok.val;
import org.bouncycastle.jcajce.provider.digest.Blake2b;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public final class SS58Codec {
    private static final byte[] PREFIX = "SS58PRE".getBytes(StandardCharsets.US_ASCII);
    private static final int CHECKSUM_LEN = 2;
    private static final int ADDRESS_LENGTH = 32;

    private SS58Codec() {
    }

    public static AddressWithPrefix decode(@NonNull String encoded) {
        Preconditions.checkArgument(encoded.length() > 0);

        val data = Base58.decode(encoded);
        if (data.length < 2) {
            throw new IllegalArgumentException("Incorrect length of data.");
        }

        int typeLen;
        short prefix;

        if (data[0] >= 0 && data[0] <= 63) {
            typeLen = 1;
            prefix = data[0];
        } else if (data[0] >= 64) {
            byte lower = (byte) (Byte.toUnsignedInt((byte) (data[0] << 2)) | Byte.toUnsignedInt((byte) (data[1] >> 6)));
            byte upper = (byte) (data[1] & 0b00111111);

            typeLen = 2;
            prefix = (short) (Byte.toUnsignedInt(lower) | (upper << 8));
        } else {
            throw new IllegalArgumentException("Unknown version.");
        }

        if (data.length != typeLen + ADDRESS_LENGTH + CHECKSUM_LEN) {
            throw new IllegalArgumentException("Incorrect length of data.");
        }

        val checkSumData = new byte[PREFIX.length + typeLen + ADDRESS_LENGTH];
        System.arraycopy(PREFIX, 0, checkSumData, 0, PREFIX.length);
        System.arraycopy(data, 0, checkSumData, PREFIX.length, typeLen + ADDRESS_LENGTH);
        val checksum = new Blake2b.Blake2b512().digest(checkSumData);
        if (checksum[0] != data[data.length - CHECKSUM_LEN] || checksum[1] != data[data.length - CHECKSUM_LEN + 1]) {
            throw new IllegalArgumentException("Incorrect checksum.");
        }

        return AddressWithPrefix.from(
                Arrays.copyOfRange(data, typeLen, typeLen + ADDRESS_LENGTH),
                SS58AddressFormat.of(prefix));
    }

    public static String encode(byte @NonNull [] address, SS58AddressFormat prefix) {
        Preconditions.checkArgument(address.length == ADDRESS_LENGTH,
                "The length of address must be 32, but was: " + address.length);

        val ident = prefix.getPrefix() & 0b0011_1111_1111_1111;
        Preconditions.checkArgument(ident == prefix.getPrefix(),
                "The prefix size is restricted by 14 bits.");

        byte[] data;
        int typeLen;

        if (ident <= 63) {
            typeLen = 1;
            data = new byte[PREFIX.length + typeLen + address.length];
            data[PREFIX.length] = (byte) ident;
        } else {
            typeLen = 2;
            data = new byte[PREFIX.length + typeLen + address.length];
            val first = (ident & 0b0000_0000_1111_1100) >> 2;
            val second = (ident >> 8) | (ident & 0b0000_0000_0000_0011) << 6;
            data[PREFIX.length] = (byte) (first | 0b01000000);
            data[PREFIX.length + 1] = (byte) second;
        }

        System.arraycopy(PREFIX, 0, data, 0, PREFIX.length);
        System.arraycopy(address, 0, data, PREFIX.length + typeLen, address.length);

        val checksum = new Blake2b.Blake2b512().digest(data);
        val encodable = new byte[typeLen + address.length + CHECKSUM_LEN];
        System.arraycopy(data, PREFIX.length, encodable, 0, typeLen + address.length);
        System.arraycopy(checksum, 0, encodable, typeLen + address.length, CHECKSUM_LEN);

        return Base58.encode(encodable);
    }
}
